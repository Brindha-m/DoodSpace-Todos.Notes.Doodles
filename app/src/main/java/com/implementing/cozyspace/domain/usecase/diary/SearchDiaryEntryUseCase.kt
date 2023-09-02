package com.implementing.cozyspace.domain.usecase.diary

import com.implementing.cozyspace.domain.repository.diary.DiaryRepository
import javax.inject.Inject

class SearchDiaryEntryUseCase @Inject constructor(
    private val diaryRepository: DiaryRepository
) {
    suspend operator fun invoke(query: String) = diaryRepository.searchEntries(query)
}