package com.example.taskmanager.domain.usecase.draft

import com.example.taskmanager.domain.model.TaskDraft
import com.example.taskmanager.domain.repository.DraftRepository
import jakarta.inject.Inject

// domain/usecase/draft/SaveDraftUseCase.kt
class SaveDraftUseCase @Inject constructor(
    private val repository: DraftRepository
) {
    suspend operator fun invoke(ownerId: String, title: String, description: String): Result<Unit> {
        if (title.isBlank()) return Result.failure(IllegalArgumentException("El título es obligatorio"))
        repository.saveDraft(
            TaskDraft(
                ownerId = ownerId,
                title = title,
                description = description,
                savedAt = System.currentTimeMillis()
            )
        )
        return Result.success(Unit)
    }
}


