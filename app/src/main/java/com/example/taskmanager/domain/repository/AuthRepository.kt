package com.example.taskmanager.domain.repository

import com.example.taskmanager.domain.model.User

// domain/repository/AuthRepository.kt
interface AuthRepository {
    suspend fun register(email: String, password: String): Result<User>
    suspend fun login(email: String, password: String): Result<User>
    fun logout()
    fun getCurrentUser(): User?
}