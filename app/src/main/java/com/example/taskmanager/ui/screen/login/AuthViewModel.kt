package com.example.taskmanager.ui.screen.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.domain.usecase.auth.GetCurrentUserUseCase
import com.example.taskmanager.domain.usecase.auth.LoginUserUseCase
import com.example.taskmanager.domain.usecase.auth.LogoutUserUseCase
import com.example.taskmanager.domain.usecase.auth.RegisterUserUseCase
import com.example.taskmanager.ui.state.AuthUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ui/screen/login/AuthViewModel.kt
@HiltViewModel
class AuthViewModel @Inject constructor(
    private val registerUserUseCase: RegisterUserUseCase,
    private val loginUserUseCase: LoginUserUseCase,
    private val logoutUserUseCase: LogoutUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    init {
        // Si ya hay una sesión activa (ej: reabriendo la app), lo detectamos aquí
        _uiState.update { it.copy(isAuthenticated = getCurrentUserUseCase() != null) }
    }

    fun register(email: String, password: String, confirmPassword: String) {
        if (password != confirmPassword) {
            _uiState.update { it.copy(errorMessage = "Las contraseñas no coinciden") }
            return
        }
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val result = registerUserUseCase(email, password)
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun login(email: String, password: String) {
        _uiState.update { it.copy(isLoading = true, errorMessage = null) }
        viewModelScope.launch {
            val result = loginUserUseCase(email, password)
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isAuthenticated = true) }
            }.onFailure { e ->
                _uiState.update { it.copy(isLoading = false, errorMessage = e.message) }
            }
        }
    }

    fun logout() {
        logoutUserUseCase()
        _uiState.update { AuthUiState(isAuthenticated = false) }
    }
}