package com.example.taskmanager.domain.repository

import com.example.taskmanager.domain.model.TaskDraft
import kotlinx.coroutines.flow.Flow

// domain/repository/DraftRepository.kt
interface DraftRepository {
    fun observeDrafts(ownerId: String): Flow<List<TaskDraft>>
    suspend fun saveDraft(draft: TaskDraft): Long
    suspend fun updateDraft(draft: TaskDraft)
    suspend fun deleteDraft(draftId: Int)
    suspend fun getDraft(draftId: Int): TaskDraft?
}