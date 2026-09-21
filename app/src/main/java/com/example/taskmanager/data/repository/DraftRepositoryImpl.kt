package com.example.taskmanager.data.repository

import com.example.taskmanager.data.local.dao.TaskDraftDao
import com.example.taskmanager.data.mapper.toDomain
import com.example.taskmanager.data.mapper.toEntity
import com.example.taskmanager.domain.model.TaskDraft
import com.example.taskmanager.domain.repository.DraftRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// data/repository/DraftRepositoryImpl.kt
class DraftRepositoryImpl @Inject constructor(
    private val dao: TaskDraftDao
) : DraftRepository {

    override fun observeDrafts(ownerId: String): Flow<List<TaskDraft>> =
        dao.observeDrafts(ownerId).map { list -> list.map { it.toDomain() } }

    override suspend fun saveDraft(draft: TaskDraft): Long = dao.insert(draft.toEntity())

    override suspend fun updateDraft(draft: TaskDraft) = dao.update(draft.toEntity())

    override suspend fun deleteDraft(draftId: Int) = dao.delete(draftId)

    override suspend fun getDraft(draftId: Int): TaskDraft? = dao.getById(draftId)?.toDomain()
}