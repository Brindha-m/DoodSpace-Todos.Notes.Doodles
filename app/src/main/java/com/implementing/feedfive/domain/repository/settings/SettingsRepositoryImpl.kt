package com.implementing.feedfive.domain.repository.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class SettingsRepositoryImpl(
    private val preferences: DataStore<Preferences>,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : SettingsRepository {

    override suspend fun <T> saveSettings(key: Preferences.Key<T>, value: T) {
        withContext(ioDispatcher) {
            preferences.edit { settings ->
                if (settings[key] != value)
                    settings[key] = value
            }
        }
    }

    override fun <T> getSettings(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return preferences.data.map { preferences -> preferences[key] ?: defaultValue }
    }
}