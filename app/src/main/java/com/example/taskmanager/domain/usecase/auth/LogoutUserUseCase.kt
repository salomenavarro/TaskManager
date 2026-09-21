package com.example.taskmanager.domain.usecase.auth

import com.example.taskmanager.domain.repository.AuthRepository
import jakarta.inject.Inject

// domain/usecase/auth/LogoutUserUseCase.kt
class LogoutUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke() = repository.logout()
}