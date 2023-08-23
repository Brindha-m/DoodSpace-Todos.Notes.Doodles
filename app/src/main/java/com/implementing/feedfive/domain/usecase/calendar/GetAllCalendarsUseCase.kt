package com.implementing.feedfive.domain.usecase.calendar

import com.implementing.feedfive.domain.repository.calendar.CalendarRepository
import com.implementing.feedfive.model.Calendar
import javax.inject.Inject

class GetAllCalendarsUseCase @Inject constructor(
    private val calendarRepository: CalendarRepository
) {
    suspend operator fun invoke(excluded: List<Int>): Map<String, List<Calendar>> {
        return calendarRepository.getCalendars().map { it.copy(included = (it.id.toInt() !in excluded)) }.groupBy { it.account }
    }
}