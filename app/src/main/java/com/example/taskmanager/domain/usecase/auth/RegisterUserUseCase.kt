package com.example.taskmanager.domain.usecase.auth

import com.example.taskmanager.domain.model.User
import com.example.taskmanager.domain.repository.AuthRepository
import jakarta.inject.Inject

// domain/usecase/auth/RegisterUserUseCase.kt
class RegisterUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        if (email.isBlank() || password.length < 6) {
            return Result.failure(IllegalArgumentException("Correo inválido o contraseña muy corta"))
        }
        return repository.register(email, password)
    }
}





