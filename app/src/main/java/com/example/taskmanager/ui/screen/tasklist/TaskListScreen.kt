package com.example.taskmanager.ui.screen.tasklist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.ui.screen.login.AuthViewModel

// ui/screen/tasklist/TaskListScreen.kt
@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    onLogout: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var newTitle by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Mis tareas", style = MaterialTheme.typography.headlineSmall)
            TextButton(onClick = {
                authViewModel.logout()
                onLogout()
            }) {
                Text("Cerrar sesión")
            }
        }

        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = newTitle,
            onValueChange = { newTitle = it },
            label = { Text("Título") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = newDescription,
            onValueChange = { newDescription = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                viewModel.addTask(newTitle, newDescription)
                newTitle = ""
                newDescription = ""
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar tarea")
        }

        Spacer(Modifier.height(16.dp))

        when {
            uiState.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.errorMessage != null -> {
                Text(
                    text = "Error: ${uiState.errorMessage}",
                    color = MaterialTheme.colorScheme.error
                )
            }
            uiState.tasks.isEmpty() -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Aún no tienes tareas. ¡Agrega la primera!")
                }
            }
            else -> {
                LazyColumn {
                    items(uiState.tasks, key = { it.id }) { task ->
                        TaskRow(
                            task = task,
                            onToggle = { viewModel.toggleCompleted(task) },
                            onDelete = { viewModel.deleteTask(task) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TaskRow(task: Task, onToggle: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(checked = task.completed, onCheckedChange = { onToggle() })
        Column(modifier = Modifier.weight(1f).padding(start = 8.dp)) {
            Text(task.title, style = MaterialTheme.typography.bodyLarge)
            if (task.description.isNotBlank()) {
                Text(task.description, style = MaterialTheme.typography.bodySmall)
            }
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
        }
    }
}