package com.implementing.cozyspace.inappscreens.diary.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.implementing.cozyspace.domain.usecase.diary.AddDiaryEntryUseCase
import com.implementing.cozyspace.domain.usecase.diary.ChartDiaryUseCase
import com.implementing.cozyspace.domain.usecase.diary.DeleteDiaryEntryUseCase
import com.implementing.cozyspace.domain.usecase.diary.GetAllDiaryEntryUseCase
import com.implementing.cozyspace.domain.usecase.diary.GetDiaryEntryUseCase
import com.implementing.cozyspace.domain.usecase.diary.SearchDiaryEntryUseCase
import com.implementing.cozyspace.domain.usecase.diary.UpdateDiaryEntryUseCase
import com.implementing.cozyspace.domain.usecase.settings.GetSettingsUseCase
import com.implementing.cozyspace.domain.usecase.settings.SaveSettingsUseCase
import com.implementing.cozyspace.inappscreens.diary.DiaryEvent
import com.implementing.cozyspace.model.Diary
import com.implementing.cozyspace.util.Constants
import com.implementing.cozyspace.util.Order
import com.implementing.cozyspace.util.OrderType
import com.implementing.cozyspace.util.toInt
import com.implementing.cozyspace.util.toOrder
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