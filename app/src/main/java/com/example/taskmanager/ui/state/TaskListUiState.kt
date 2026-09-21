package com.example.taskmanager.ui.state

import com.example.taskmanager.domain.model.Task

// ui/state/TaskListUiState.kt
data class TaskListUiState(
    val isLoading: Boolean = true,
    val tasks: List<Task> = emptyList(),
    val errorMessage: String? = null
)