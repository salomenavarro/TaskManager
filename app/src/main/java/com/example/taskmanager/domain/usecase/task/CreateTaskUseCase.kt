package com.example.taskmanager.domain.usecase.task

import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.domain.repository.TaskRepository
import jakarta.inject.Inject

// domain/usecase/task/CreateTaskUseCase.kt
class CreateTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(ownerId: String, title: String, description: String): Result<Unit> {
        if (title.isBlank()) return Result.failure(IllegalArgumentException("El título es obligatorio"))
        val now = System.currentTimeMillis()
        val task = Task(
            ownerId = ownerId,
            title = title,
            description = description,
            completed = false,
            createdAt = now,
            updatedAt = now
        )
        return repository.createTask(task)
    }
}

