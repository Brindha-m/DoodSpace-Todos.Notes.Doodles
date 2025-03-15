package com.implementing.cozyspace.firebase.remoteConfig.wrapper

import android.util.Log
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import com.google.gson.Gson


class RemoteConfigWrapper(private val firebaseRemoteConfig: FirebaseRemoteConfig) {

    fun initRemoteConfig() {
        val configSettings = FirebaseRemoteConfigSettings.Builder()
            .setMinimumFetchIntervalInSeconds(60)
            .build()

        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)

        // Set default values (optional fallback)
        val defaultFestiveImagesJson = Gson().toJson(
            listOf(
                "https://raw.githubusercontent.com/Brindhamanick/cozy_notifications/main/dood_remote/mihitman.png",
                "https://raw.githubusercontent.com/Brindhamanick/cozy_notifications/main/dood_remote/rcbsm.png",
                "https://raw.githubusercontent.com/Brindhamanick/cozy_notifications/main/dood_remote/sixipl.png"
            )
        )

        firebaseRemoteConfig.setDefaultsAsync(
            mapOf(
                "festive_mode" to true,
                "festive_images" to defaultFestiveImagesJson
            )
        )
    }

    // Fetch festive mode state
    fun fetchSpecialState(specialShowState: (Boolean) -> Unit) {
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener {
            specialShowState(firebaseRemoteConfig.getBoolean("festive_mode"))
        }.addOnFailureListener {
            Log.d(TAG, it.message.orEmpty())
        }
    }

    // Fetch festive images JSON
    fun fetchFestiveImagesJson(callback: (String) -> Unit) {
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val jsonString = firebaseRemoteConfig.getString("festive_images")
                callback(jsonString)
            } else {
                Log.d(TAG, "Failed to fetch festive images: ${task.exception?.message.orEmpty()}")
            }
        }.addOnFailureListener {
            Log.d(TAG, "Fetch failed: ${it.message.orEmpty()}")
        }
    }

    companion object {
        private const val TAG = "RemoteConfigWrapper"
    }
}