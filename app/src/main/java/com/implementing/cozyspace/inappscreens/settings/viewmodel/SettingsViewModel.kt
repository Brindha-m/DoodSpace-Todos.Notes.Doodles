package com.implementing.cozyspace.inappscreens.settings.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.implementing.cozyspace.domain.usecase.settings.GetSettingsUseCase
import com.implementing.cozyspace.domain.usecase.settings.SaveSettingsUseCase
import com.implementing.cozyspace.inappscreens.bookmark.viewmodel.BookmarksViewModel
import com.implementing.cozyspace.model.Bookmark
import com.implementing.cozyspace.util.ItemView
import com.implementing.cozyspace.util.Order
import com.implementing.cozyspace.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val saveSettingsUseCase: SaveSettingsUseCase,
    private val getSettingsUseCase: GetSettingsUseCase
): ViewModel() {

    fun <T> getSettings(key: Preferences.Key<T>, defaultValue: T): Flow<T> {
        return getSettingsUseCase(key, defaultValue)
    }

    fun <T> saveSettings(key: Preferences.Key<T>, value: T) {
        viewModelScope.launch {
            saveSettingsUseCase(key, value)
        }
    }
}