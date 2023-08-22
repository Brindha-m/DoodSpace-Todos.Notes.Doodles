package com.implementing.feedfive

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.StringRes
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.implementing.feedfive.util.Constants
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = Constants.SETTINGS_PREFERENCES)


@HiltAndroidApp
class FiveHiltMain : Application(), Configuration.Provider {
    companion object {
        lateinit var appContext: Context
    }

    @Inject
    lateinit var workerFactory: HiltWorkerFactory


    override fun getWorkManagerConfiguration() =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()


    override fun onCreate() {
        super.onCreate()
        appContext = this
        createRemindersNotificationChannel()
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

}

// for string resources where context is not available
fun getString(@StringRes resId: Int, vararg args: String) =
    FiveHiltMain.appContext.getString(resId, *args)
