package com.example.taskmanager.ui.state

import com.example.taskmanager.domain.model.TaskDraft

// ui/state/DraftListUiState.kt
data class DraftListUiState(
    val isLoading: Boolean = true,
    val drafts: List<TaskDraft> = emptyList(),
    val errorMessage: String? = null,
    val publishError: String? = null
)