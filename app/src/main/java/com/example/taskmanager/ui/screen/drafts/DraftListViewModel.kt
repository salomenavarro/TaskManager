package com.example.taskmanager.ui.screen.drafts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.domain.model.TaskDraft
import com.example.taskmanager.domain.usecase.auth.GetCurrentUserUseCase
import com.example.taskmanager.domain.usecase.auth.LoginUserUseCase
import com.example.taskmanager.domain.usecase.auth.LogoutUserUseCase
import com.example.taskmanager.domain.usecase.auth.RegisterUserUseCase
import com.example.taskmanager.domain.usecase.draft.DeleteDraftUseCase
import com.example.taskmanager.domain.usecase.draft.GetDraftsUseCase
import com.example.taskmanager.domain.usecase.draft.PublishDraftUseCase
import com.example.taskmanager.domain.usecase.draft.SaveDraftUseCase
import com.example.taskmanager.ui.state.AuthUiState
import com.example.taskmanager.ui.state.DraftListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ui/screen/drafts/DraftListViewModel.kt
@HiltViewModel
class DraftListViewModel @Inject constructor(
    private val getDraftsUseCase: GetDraftsUseCase,
    private val saveDraftUseCase: SaveDraftUseCase,
    private val deleteDraftUseCase: DeleteDraftUseCase,
    private val publishDraftUseCase: PublishDraftUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DraftListUiState())
    val uiState: StateFlow<DraftListUiState> = _uiState.asStateFlow()

    private val ownerId: String get() = getCurrentUserUseCase()?.uid.orEmpty()

    init {
        viewModelScope.launch {
            getDraftsUseCase(ownerId)
                .catch { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
                .collect { drafts -> _uiState.update { it.copy(drafts = drafts, isLoading = false) } }
        }
    }

    fun saveDraft(title: String, description: String) {
        viewModelScope.launch { saveDraftUseCase(ownerId, title, description) }
    }

    fun deleteDraft(draft: TaskDraft) {
        viewModelScope.launch { deleteDraftUseCase(draft.id) }
    }

    fun publishDraft(draft: TaskDraft) {
        viewModelScope.launch {
            publishDraftUseCase(draft).onFailure { e ->
                _uiState.update { it.copy(publishError = e.message) }
            }
        }
    }

    fun dismissPublishError() {
        _uiState.update { it.copy(publishError = null) }
    }
}