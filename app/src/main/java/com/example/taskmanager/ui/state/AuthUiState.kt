package com.example.taskmanager.ui.state

// ui/state/AuthUiState.kt
data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isAuthenticated: Boolean = false
)