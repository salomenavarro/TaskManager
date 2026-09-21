package com.example.taskmanager.domain.usecase.draft

import com.example.taskmanager.domain.model.TaskDraft
import com.example.taskmanager.domain.repository.DraftRepository
import jakarta.inject.Inject

// domain/usecase/draft/UpdateDraftUseCase.kt
class UpdateDraftUseCase @Inject constructor(
    private val repository: DraftRepository
) {
    suspend operator fun invoke(draft: TaskDraft) = repository.updateDraft(draft)
}
