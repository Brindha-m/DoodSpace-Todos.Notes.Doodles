package com.implementing.cozyspace.di

import com.implementing.cozyspace.domain.usecase.calendar.GetAllCalendarEventsUseCase
import com.implementing.cozyspace.domain.usecase.settings.GetSettingsUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface CalendarWidgetEntryPoint {
    fun getSettingsUseCase(): GetSettingsUseCase

    fun getAllEventsUseCase(): GetAllCalendarEventsUseCase
}