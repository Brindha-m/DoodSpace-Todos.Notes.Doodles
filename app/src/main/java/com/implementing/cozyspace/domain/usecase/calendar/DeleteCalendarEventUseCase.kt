package com.implementing.cozyspace.domain.usecase.calendar

import android.content.Context
import android.content.Intent
import com.implementing.cozyspace.domain.repository.calendar.CalendarRepository
import com.implementing.cozyspace.inappscreens.glance_widgets.calendar.RefreshCalendarWidgetReceiver
import com.implementing.cozyspace.model.CalendarEvent
import javax.inject.Inject

class DeleteCalendarEventUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository,
    private val context: Context
) {
    suspend operator fun invoke(event: CalendarEvent) {
        calendarRepository.deleteEvent(event)
        val updateIntent = Intent(context, RefreshCalendarWidgetReceiver::class.java)
        context.sendBroadcast(updateIntent)
    }
}