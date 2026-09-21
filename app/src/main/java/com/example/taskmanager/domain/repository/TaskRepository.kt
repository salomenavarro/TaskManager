package com.example.taskmanager.domain.repository

import com.example.taskmanager.domain.model.Task
import kotlinx.coroutines.flow.Flow


// domain/repository/TaskRepository.kt
interface TaskRepository {
    fun observeTasks(ownerId: String): Flow<List<Task>>
    suspend fun createTask(task: Task): Result<Unit>
    suspend fun updateTask(taskId: String, changes: Map<String, Any>): Result<Unit>
    suspend fun deleteTask(taskId: String): Result<Unit>
}