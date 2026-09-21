package com.example.taskmanager.domain.usecase.task

import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.domain.repository.TaskRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

// domain/usecase/task/GetTasksUseCase.kt
class GetTasksUseCase @Inject constructor(
    private val repository: TaskRepository
) {
    operator fun invoke(ownerId: String): Flow<List<Task>> = repository.observeTasks(ownerId)
}

