package com.example.taskmanager.ui.state

import com.example.taskmanager.domain.model.TaskDraft

data class DraftListUiState(
    val isLoading: Boolean = true,
    val drafts: List<TaskDraft> = emptyList(),
    val errorMessage: String? = null,
    val publishError: String? = null,
    val publishingDraftId: Int? = null // Bloquea el borrador específico mientras se envía a Firestore
)