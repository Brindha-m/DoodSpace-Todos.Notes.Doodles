package com.implementing.feedfive.inappscreens.diary

import com.implementing.feedfive.model.Diary
import com.implementing.feedfive.util.Order

sealed class DiaryEvent {
    data class AddEntry(val entry: Diary) : DiaryEvent()
    data class GetEntry(val entryId: Int) : DiaryEvent()
    data class SearchEntries(val query: String) : DiaryEvent()
    data class UpdateOrder(val order: Order) : DiaryEvent()
    data class UpdateEntry(val entry: Diary) : DiaryEvent()
    data class DeleteEntry(val entry: Diary) : DiaryEvent()

    data class ChangeChartEntriesRange(val monthly: Boolean) : DiaryEvent()
    object ErrorDisplayed: DiaryEvent()
}