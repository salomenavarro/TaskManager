package com.example.taskmanager.domain.usecase.task

import com.example.taskmanager.domain.repository.TaskRepository
import jakarta.inject.Inject

// domain/usecase/task/DeleteTaskUseCase.kt
class DeleteTaskUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    suspend operator fun invoke(taskId: String): Result<Unit> = repository.deleteTask(taskId)
}