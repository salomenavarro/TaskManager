package com.example.taskmanager.domain.usecase.auth

import com.example.taskmanager.domain.model.User
import com.example.taskmanager.domain.repository.AuthRepository
import jakarta.inject.Inject

// domain/usecase/auth/LoginUserUseCase.kt
class LoginUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return repository.login(email, password)
    }
}