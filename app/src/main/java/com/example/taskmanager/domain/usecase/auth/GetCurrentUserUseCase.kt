package com.example.taskmanager.domain.usecase.auth

import com.example.taskmanager.domain.model.User
import com.example.taskmanager.domain.repository.AuthRepository
import jakarta.inject.Inject

// domain/usecase/auth/GetCurrentUserUseCase.kt
class GetCurrentUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): User? = repository.getCurrentUser()
}