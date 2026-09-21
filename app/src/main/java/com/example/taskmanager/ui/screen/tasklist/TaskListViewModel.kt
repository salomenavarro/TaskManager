package com.example.taskmanager.ui.screen.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.domain.usecase.auth.GetCurrentUserUseCase
import com.example.taskmanager.domain.usecase.task.CreateTaskUseCase
import com.example.taskmanager.domain.usecase.task.DeleteTaskUseCase
import com.example.taskmanager.domain.usecase.task.GetTasksUseCase
import com.example.taskmanager.domain.usecase.task.UpdateTaskUseCase
import com.example.taskmanager.ui.state.TaskListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// ui/screen/tasklist/TaskListViewModel.kt
@HiltViewModel
class TaskListViewModel @Inject constructor(
    private val getTasksUseCase: GetTasksUseCase,
    private val createTaskUseCase: CreateTaskUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskListUiState())
    val uiState: StateFlow<TaskListUiState> = _uiState.asStateFlow()

    private val ownerId: String get() = getCurrentUserUseCase()?.uid.orEmpty()

    init {
        observeTasks()
    }

    private fun observeTasks() {
        if (ownerId.isBlank()) {
            _uiState.update { it.copy(isLoading = false, errorMessage = "No hay sesión activa") }
            return
        }
        viewModelScope.launch {
            getTasksUseCase(ownerId)
                .catch { e -> _uiState.update { it.copy(isLoading = false, errorMessage = e.message) } }
                .collect { tasks ->
                    _uiState.update { it.copy(tasks = tasks, isLoading = false, errorMessage = null) }
                }
        }
    }

    fun addTask(title: String, description: String) {
        viewModelScope.launch {
            createTaskUseCase(ownerId, title, description)
                .onFailure { e -> _uiState.update { it.copy(errorMessage = e.message) } }
        }
    }

    fun toggleCompleted(task: Task) {
        viewModelScope.launch {
            updateTaskUseCase(task.id, task.title, task.description, !task.completed)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            deleteTaskUseCase(task.id)
        }
    }
}