package com.implementing.cozyspace.domain.repository.settings

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface SettingsRepository {

    suspend fun <T> saveSettings(key: Preferences.Key<T>, value: T)

    fun <T> getSettings(key: Preferences.Key<T>, defaultValue: T): Flow<T>

}