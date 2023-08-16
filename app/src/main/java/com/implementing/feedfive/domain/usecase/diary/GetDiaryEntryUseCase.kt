package com.implementing.feedfive.domain.usecase.diary

import com.implementing.feedfive.domain.repository.diary.DiaryRepository
import javax.inject.Inject

class GetDiaryEntryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
){
    suspend operator fun invoke(id: Int) = diaryRepository.getEntry(id)
}