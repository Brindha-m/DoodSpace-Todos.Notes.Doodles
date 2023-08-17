package com.implementing.feedfive.domain.usecase.diary

import com.implementing.feedfive.domain.repository.diary.DiaryRepository
import com.implementing.feedfive.model.Diary
import com.implementing.feedfive.util.inTheLast30Days
import com.implementing.feedfive.util.inTheLastYear
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