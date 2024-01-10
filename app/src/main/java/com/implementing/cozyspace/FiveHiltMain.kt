package com.implementing.cozyspace

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.annotation.StringRes
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.messaging.FirebaseMessaging
import com.implementing.cozyspace.util.Constants
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.SETTINGS_PREFERENCES)


@HiltAndroidApp
class FiveHiltMain : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    // Add this property to fulfill the requirements of Configuration.Provider
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()


    companion object {
        lateinit var appContext: Context
        private const val TAG = "FiveHiltMain"

    }


    override fun onCreate() {
        super.onCreate()
        appContext = this
        createRemindersNotificationChannel()
        FirebaseApp.initializeApp(this)
        // To get the refreshed token, just comment the about func
//        logRegToken()

    }
    // Notification
    private fun createRemindersNotificationChannel() {
        val channel = NotificationChannel(
            Constants.REMINDERS_CHANNEL_ID,
            getString(R.string.reminders_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT
        )
        channel.description = getString(R.string.reminders_channel_description)
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun logRegToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(ContentValues.TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result

            // Log and toast
            val msg = "FCM Registration token: $token"
            Log.d(TAG, msg)
//            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })

    }

//    private fun firebase_config() {
//        val remoteConfig: FirebaseRemoteConfig = Firebase.remoteConfig
//        val configSettings = remoteConfigSettings {
//            minimumFetchIntervalInSeconds = 3600
//        }
//        remoteConfig.setConfigSettingsAsync(configSettings)
//        remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults) // Create an XML file in res/xml/ with default values
//        // Fetch Remote Config values
//        remoteConfig.fetchAndActivate().addOnCompleteListener {  }
//            .addOnCompleteListener() { task ->
//                if (task.isSuccessful) {
//                    val updated = task.result
//                    Log.d(TAG, "Config params updated: $updated")
//                    Toast.makeText(this, "Fetch and activate succeeded",
//                        Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this, "Fetch failed",
//                        Toast.LENGTH_SHORT).show()
//                }
//
//            }
//    }



}



// for string resources where context is not available
fun getString(@StringRes resId: Int, vararg args: String) =
    FiveHiltMain.appContext.getString(resId, *args)



