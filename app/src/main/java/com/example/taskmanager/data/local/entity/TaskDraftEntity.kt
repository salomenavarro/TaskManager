package com.example.taskmanager.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// data/local/entity/TaskDraftEntity.kt
@Entity(tableName = "task_drafts")
data class TaskDraftEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val ownerId: String,
    val title: String,
    val description: String,
    val savedAt: Long
)