package com.example.taskmanager.domain.usecase.draft

import com.example.taskmanager.domain.repository.DraftRepository
import jakarta.inject.Inject

// domain/usecase/draft/DeleteDraftUseCase.kt
class DeleteDraftUseCase @Inject constructor(
    private val repository: DraftRepository
) {
    suspend operator fun invoke(draftId: Int) = repository.deleteDraft(draftId)
}