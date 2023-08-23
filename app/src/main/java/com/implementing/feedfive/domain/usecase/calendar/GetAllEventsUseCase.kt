package com.implementing.feedfive.domain.usecase.calendar


import com.implementing.feedfive.domain.repository.calendar.CalendarRepository
import com.implementing.feedfive.model.CalendarEvent
import com.implementing.feedfive.util.formatDateForMapping
import javax.inject.Inject

class GetAllEventsUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {
    suspend operator fun invoke(excluded: List<Int>, fromWidget: Boolean = false): Map<String, List<CalendarEvent>> {
        val events =  calendarRepository.getEvents()
            .filter { it.calendarId.toInt() !in excluded }
        return if (fromWidget)
            events.take(60).groupBy { event ->
                event.start.formatDateForMapping()
            }
        else
            events.groupBy { event ->
                event.start.formatDateForMapping()
            }

    }
}