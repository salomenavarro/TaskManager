package com.example.taskmanager.domain.usecase.task

import com.example.taskmanager.domain.repository.TaskRepository
import jakarta.inject.Inject

// domain/usecase/task/UpdateTaskUseCase.kt
class UpdateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: String, title: String, description: String, completed: Boolean): Result<Unit> {
        val changes = mapOf(
            "title" to title,
            "description" to description,
            "completed" to completed,
            "updatedAt" to System.currentTimeMillis()
        )
        return repository.updateTask(taskId, changes)
    }
}

