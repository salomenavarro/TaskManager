package com.example.taskmanager.domain.usecase.draft

import com.example.taskmanager.domain.model.TaskDraft
import com.example.taskmanager.domain.repository.DraftRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow


// domain/usecase/draft/GetDraftsUseCase.kt
class GetDraftsUseCase @Inject constructor(
    private val repository: DraftRepository
) {
    operator fun invoke(ownerId: String): Flow<List<TaskDraft>> = repository.observeDrafts(ownerId)
}
