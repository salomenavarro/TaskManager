// Reemplaza: app/src/main/java/com/example/taskmanager/domain/usecase/auth/RegisterUserUseCase.kt
package com.example.taskmanager.domain.usecase.auth

import com.example.taskmanager.domain.model.User
import com.example.taskmanager.domain.repository.AuthRepository
import jakarta.inject.Inject
import android.util.Patterns

// domain/usecase/auth/RegisterUserUseCase.kt
class RegisterUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        // FIX (RF01): el taller pide validar "formato del correo", no solo que no
        // esté vacío. Antes "abc" pasaba la validación del cliente.
        if (email.isBlank() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            return Result.failure(IllegalArgumentException("Ingresa un correo con formato válido"))
        }
        if (password.length < 6) {
            return Result.failure(IllegalArgumentException("La contraseña debe tener mínimo 6 caracteres"))
        }
        return repository.register(email, password)
    }
}