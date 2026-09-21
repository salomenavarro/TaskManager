package com.example.taskmanager.data.mapper

import com.example.taskmanager.data.local.entity.TaskDraftEntity
import com.example.taskmanager.domain.model.TaskDraft

// data/mapper/TaskDraftMapper.kt
fun TaskDraftEntity.toDomain(): TaskDraft = TaskDraft(
    id = id,
    ownerId = ownerId,
    title = title,
    description = description,
    savedAt = savedAt
)

fun TaskDraft.toEntity(): TaskDraftEntity = TaskDraftEntity(
    id = id,
    ownerId = ownerId,
    title = title,
    description = description,
    savedAt = savedAt
)