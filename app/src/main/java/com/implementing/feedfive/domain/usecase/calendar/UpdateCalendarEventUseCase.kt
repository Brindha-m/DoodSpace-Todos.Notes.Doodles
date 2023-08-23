package com.implementing.feedfive.domain.usecase.calendar

import android.content.Context
import android.content.Intent
import com.implementing.feedfive.domain.repository.calendar.CalendarRepository
import com.implementing.feedfive.inappscreens.glance_widgets.calendar.RefreshCalendarWidgetReceiver
import com.implementing.feedfive.model.CalendarEvent
import javax.inject.Inject

class UpdateCalendarEventUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository,
    private val context: Context
) {
    suspend operator fun invoke(event: CalendarEvent) {
        calendarRepository.updateEvent(event)
        val updateIntent = Intent(context, RefreshCalendarWidgetReceiver::class.java)
        context.sendBroadcast(updateIntent)
    }
}