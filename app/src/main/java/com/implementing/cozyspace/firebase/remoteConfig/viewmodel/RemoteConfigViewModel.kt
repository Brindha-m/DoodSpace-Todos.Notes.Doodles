package com.implementing.cozyspace.firebase.remoteConfig.viewmodel

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.gson.Gson
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.implementing.cozyspace.firebase.remoteConfig.event.EventFetchState
import com.implementing.cozyspace.firebase.remoteConfig.model.EventModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class EventViewModel @Inject constructor(
    private val firebaseRemoteConfig: FirebaseRemoteConfig
) : ViewModel() {

    private val _events = MutableStateFlow<EventFetchState>(EventFetchState.Loading)
    val events: StateFlow<EventFetchState> get() = _events

    init {
        fetchRemoteConfig()
    }

    private fun fetchRemoteConfig() {
        // Set default values (optional, for offline fallback)
        val defaults = mapOf(
            "festive_images" to "[]", // Empty JSON array as default
            "festive_mode" to true
        )
        firebaseRemoteConfig.setDefaultsAsync(defaults)
            .addOnCompleteListener { defaultTask ->
                if (!defaultTask.isSuccessful) {
                    Log.e(TAG, "Failed to set defaults: ${defaultTask.exception?.message}")
                }

                // Fetch and activate Remote Config
                firebaseRemoteConfig.fetchAndActivate()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val jsonString = firebaseRemoteConfig.getString("festive_images")
                            val eventsList = parseJson(jsonString)
                            val festiveMode = firebaseRemoteConfig.getBoolean("festive_mode")
                            Log.d(TAG, "Fetched JSON: $jsonString, Festive Mode: $festiveMode")

                            _events.value = if (festiveMode) {
                                EventFetchState.Success(eventsList)
                            } else {
                                EventFetchState.Error("Festive mode is OFF")
                            }
                        } else {
                            val errorMessage = task.exception?.message ?: "Unknown error"
                            Log.e(TAG, "Fetch failed: $errorMessage", task.exception)
                            _events.value = EventFetchState.Error("Failed to fetch: $errorMessage")
                        }
                    }
            }
    }

    private fun parseJson(jsonString: String): List<EventModel> {
        return try {
            val gson = Gson()
            val typeToken = object : TypeToken<List<EventModel>>() {}.type
            gson.fromJson(jsonString, typeToken) ?: emptyList()
        } catch (e: JsonSyntaxException) {
            Log.e(TAG, "JSON parsing failed: ${e.message}")
            emptyList()
        }
    }
}
