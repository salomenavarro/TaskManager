package com.example.taskmanager.domain.usecase.draft

import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.domain.model.TaskDraft
import com.example.taskmanager.domain.repository.DraftRepository
import com.example.taskmanager.domain.repository.TaskRepository
import jakarta.inject.Inject

// domain/usecase/draft/PublishDraftUseCase.kt
class PublishDraftUseCase @Inject constructor(
    private val taskRepository: TaskRepository,
    private val draftRepository: DraftRepository
) {
    suspend operator fun invoke(draft: TaskDraft): Result<Unit> {
        val now = System.currentTimeMillis()
        val task = Task(
            ownerId = draft.ownerId,
            title = draft.title,
            description = draft.description,
            completed = false,
            createdAt = now,
            updatedAt = now
        )
        // 1. Crear en Firestore y esperar confirmación
        val result = taskRepository.createTask(task)

        // 2. Solo si fue exitoso, eliminar el borrador local
        return result.onSuccess {
            draftRepository.deleteDraft(draft.id)
        }
        // 3. Si falla, no se toca Room — el borrador se conserva automáticamente
    }
}