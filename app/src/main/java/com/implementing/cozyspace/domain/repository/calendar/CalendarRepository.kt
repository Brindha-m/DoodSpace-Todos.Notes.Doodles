package com.implementing.cozyspace.domain.repository.calendar

import com.implementing.cozyspace.model.Calendar
import com.implementing.cozyspace.model.CalendarEvent

interface CalendarRepository {
    suspend fun getEvents(): List<CalendarEvent>
    suspend fun getCalendars(): List<Calendar>

    suspend fun addEvent(event: CalendarEvent)

    suspend fun deleteEvent(event: CalendarEvent)

    suspend fun updateEvent(event: CalendarEvent)
}