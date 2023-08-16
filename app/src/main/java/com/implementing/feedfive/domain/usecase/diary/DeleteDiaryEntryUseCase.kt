package com.implementing.feedfive.domain.usecase.diary

import com.implementing.feedfive.domain.repository.diary.DiaryRepository
import com.implementing.feedfive.model.Diary
import javax.inject.Inject

class DeleteDiaryEntryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(diaryEntry: Diary) = diaryRepository.deleteEntry(diaryEntry)
}