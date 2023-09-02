package com.implementing.cozyspace.domain.usecase.diary

import com.implementing.cozyspace.domain.repository.diary.DiaryRepository
import com.implementing.cozyspace.model.Diary
import com.implementing.cozyspace.util.inTheLast30Days
import com.implementing.cozyspace.util.inTheLastYear
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class ChartDiaryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
){
    suspend operator fun invoke(monthly: Boolean): List<Diary> {
        return diaryRepository
            .getAllEntries()
            .first()
            .filter {
                if (monthly) it.createdDate.inTheLast30Days()
                else it.createdDate.inTheLastYear()
            }
            .sortedBy { it.createdDate }
    }
}