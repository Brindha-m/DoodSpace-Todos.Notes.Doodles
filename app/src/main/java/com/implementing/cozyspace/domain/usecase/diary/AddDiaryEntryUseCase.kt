package com.implementing.cozyspace.domain.usecase.diary

import com.implementing.cozyspace.domain.repository.diary.DiaryRepository
import com.implementing.cozyspace.model.Diary
import javax.inject.Inject

class AddDiaryEntryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(diaryEntry: Diary) = diaryRepository.addEntry(diaryEntry)
}