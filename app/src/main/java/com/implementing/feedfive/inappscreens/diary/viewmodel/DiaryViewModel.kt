package com.implementing.feedfive.inappscreens.diary.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.implementing.feedfive.domain.usecase.diary.AddDiaryEntryUseCase
import com.implementing.feedfive.domain.usecase.diary.ChartDiaryUseCase
import com.implementing.feedfive.domain.usecase.diary.DeleteDiaryEntryUseCase
import com.implementing.feedfive.domain.usecase.diary.GetAllDiaryEntryUseCase
import com.implementing.feedfive.domain.usecase.diary.GetDiaryEntryUseCase
import com.implementing.feedfive.domain.usecase.diary.SearchDiaryEntryUseCase
import com.implementing.feedfive.domain.usecase.diary.UpdateDiaryEntryUseCase
import com.implementing.feedfive.domain.usecase.settings.GetSettingsUseCase
import com.implementing.feedfive.domain.usecase.settings.SaveSettingsUseCase
import com.implementing.feedfive.inappscreens.diary.DiaryEvent
import com.implementing.feedfive.model.Diary
import com.implementing.feedfive.util.Constants
import com.implementing.feedfive.util.Order
import com.implementing.feedfive.util.OrderType
import com.implementing.feedfive.util.toInt
import com.implementing.feedfive.util.toOrder
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class DiaryViewModel @Inject constructor(
    private val addEntry: AddDiaryEntryUseCase,
    private val updateEntry: UpdateDiaryEntryUseCase,
    private val deleteEntry: DeleteDiaryEntryUseCase,
    private val getAlEntries: GetAllDiaryEntryUseCase,
    private val searchEntries: SearchDiaryEntryUseCase,
    private val getEntry: GetDiaryEntryUseCase,
    private val getSettings: GetSettingsUseCase,
    private val saveSettings: SaveSettingsUseCase,
    private val getEntriesForChart: ChartDiaryUseCase
): ViewModel() {
    var uiState by mutableStateOf(UiState())
        private set

    private var getDiaryEntriesJob: Job? = null

    init {
        viewModelScope.launch {
            getSettings(
                intPreferencesKey(Constants.DIARY_ORDER_KEY),
                Order.DateModified(OrderType.ASC()).toInt()
            ).collect {
                getEntries(it.toOrder())
            }
        }
    }

    fun onEvent(event: DiaryEvent) {
        when (event) {
            is DiaryEvent.AddEntry -> viewModelScope.launch {
                addEntry(event.entry)
                uiState = uiState.copy(
                    navigateUp = true
                )
            }
            is DiaryEvent.DeleteEntry -> viewModelScope.launch {
                deleteEntry(event.entry)
                uiState = uiState.copy(
                    navigateUp = true
                )
            }
            is DiaryEvent.GetEntry -> viewModelScope.launch {
                val entry = getEntry(event.entryId)
                uiState = uiState.copy(
                    entry = entry
                )
            }
            is DiaryEvent.SearchEntries -> viewModelScope.launch {
                val entries = searchEntries(event.query)
                uiState = uiState.copy(
                    searchEntries = entries
                )
            }
            is DiaryEvent.UpdateEntry -> viewModelScope.launch {
                updateEntry(event.entry)
                uiState = uiState.copy(
                    navigateUp = true
                )
            }
            is DiaryEvent.UpdateOrder -> viewModelScope.launch {
                saveSettings(
                    intPreferencesKey(Constants.DIARY_ORDER_KEY),
                    event.order.toInt()
                )
            }
            DiaryEvent.ErrorDisplayed -> uiState = uiState.copy(error = null)
            is DiaryEvent.ChangeChartEntriesRange -> viewModelScope.launch {
                uiState = uiState.copy(chartEntries = getEntriesForChart(event.monthly))
            }
        }
    }

    data class UiState(
        val entries: List<Diary> = emptyList(),
        val entriesOrder: Order = Order.DateModified(OrderType.ASC()),
        val entry: Diary? = null,
        val error: String? = null,
        val searchEntries: List<Diary> = emptyList(),
        val navigateUp: Boolean = false,
        val chartEntries : List<Diary> = emptyList()
    )

    private fun getEntries(order: Order) {
        getDiaryEntriesJob?.cancel()
        getDiaryEntriesJob = getAlEntries(order)
            .onEach { entries ->
                uiState = uiState.copy(
                    entries = entries,
                    entriesOrder = order
                )
            }.launchIn(viewModelScope)
    }
}