package com.implementing.feedfive.inappscreens.calendar

import com.implementing.feedfive.model.Calendar
import com.implementing.feedfive.model.CalendarEvent

sealed class CalendarEventsVM {
    data class IncludeCalendar(val calendar: Calendar) : CalendarEventsVM()
    data class ReadPermissionChanged(val hasPermission: Boolean) : CalendarEventsVM()
    data class EditEvent(val event: CalendarEvent) : CalendarEventsVM()
    data class DeleteEvent(val event: CalendarEvent) : CalendarEventsVM()
    data class AddEvent(val event: CalendarEvent) : CalendarEventsVM()
    object ErrorDisplayed : CalendarEventsVM()
}