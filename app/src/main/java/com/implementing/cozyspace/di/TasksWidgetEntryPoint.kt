package com.implementing.cozyspace.di

import com.implementing.cozyspace.domain.usecase.settings.GetSettingsUseCase
import com.implementing.cozyspace.domain.usecase.tasks.GetAllTasksUseCase
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface TasksWidgetEntryPoint {
    fun getSettingsUseCase(): GetSettingsUseCase

    fun getAllTasksUseCase(): GetAllTasksUseCase
}