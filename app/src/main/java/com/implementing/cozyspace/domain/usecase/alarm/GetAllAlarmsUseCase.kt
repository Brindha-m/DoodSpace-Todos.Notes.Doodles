package com.implementing.cozyspace.domain.usecase.alarm


import com.implementing.cozyspace.domain.repository.alarm.AlarmRepository
import javax.inject.Inject

class GetAllAlarmsUseCase @Inject constructor(
    private val alarmRepository: AlarmRepository
) {
    suspend operator fun invoke() = alarmRepository.getAlarms()
}