package com.implementing.feedfive.domain.usecase.settings

import androidx.datastore.preferences.core.Preferences
import com.implementing.feedfive.domain.repository.settings.SettingsRepository
import javax.inject.Inject

class GetSettingsUseCase @Inject constructor(
    private val settingsRepository: SettingsRepository
) {
    operator fun <T> invoke(key: Preferences.Key<T>, defaultValue: T) = settingsRepository.getSettings(key, defaultValue)
}