package com.implementing.cozyspace.domain.usecase.settings

import androidx.datastore.preferences.core.Preferences
import com.implementing.cozyspace.domain.repository.settings.SettingsRepository
import javax.inject.Inject

class SaveSettingsUseCase @Inject constructor(
  private val settingsRepository: SettingsRepository
) {
    suspend operator fun <T> invoke(key: Preferences.Key<T>, value: T) = settingsRepository.saveSettings(key, value)
}