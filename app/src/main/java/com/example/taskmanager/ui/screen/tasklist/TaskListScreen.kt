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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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

@Composable
fun TaskListScreen(
    viewModel: TaskListViewModel = hiltViewModel(),
    authViewModel: AuthViewModel = hiltViewModel(),
    onLogout: () -> Unit,
    onNavigateToDrafts: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var newTitle by remember { mutableStateOf("") }
    var newDescription by remember { mutableStateOf("") }

    var taskPendingDelete by remember { mutableStateOf<Task?>(null) }
    var taskPendingEdit by remember { mutableStateOf<Task?>(null) } // <-- Estado de edición

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Mis tareas", style = MaterialTheme.typography.headlineSmall)
            Row {
                TextButton(onClick = onNavigateToDrafts) {
                    Text("Borradores")
                }
                TextButton(onClick = {
                    authViewModel.logout()
                    onLogout()
                }) {
                    Text("Cerrar sesión")
                }
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

        Row(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    viewModel.addTask(newTitle, newDescription)
                    newTitle = ""
                    newDescription = ""
                },
                enabled = newTitle.isNotBlank(), // Validación contra espacios en blanco
                modifier = Modifier.weight(1f)
            ) {
                Text("Agregar")
            }
            Spacer(Modifier.width(8.dp))
            OutlinedButton(
                onClick = {
                    viewModel.saveDraft(newTitle, newDescription)
                    newTitle = ""
                    newDescription = ""
                },
                enabled = newTitle.isNotBlank(), // Validación contra espacios en blanco
                modifier = Modifier.weight(1f)
            ) {
                Text("Borrador")
            }
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
                            onEdit = { taskPendingEdit = task },
                            onDelete = { taskPendingDelete = task }
                        )
                    }
                }
            }
        }
    }

    // Modal de Edición de Tarea
    taskPendingEdit?.let { task ->
        var editTitle by remember { mutableStateOf(task.title) }
        var editDescription by remember { mutableStateOf(task.description) }

        AlertDialog(
            onDismissRequest = { taskPendingEdit = null },
            title = { Text("Editar tarea") },
            text = {
                Column {
                    OutlinedTextField(
                        value = editTitle,
                        onValueChange = { editTitle = it },
                        label = { Text("Título") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = editDescription,
                        onValueChange = { editDescription = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.updateTaskDetails(task, editTitle, editDescription)
                        taskPendingEdit = null
                    },
                    enabled = editTitle.isNotBlank() // No permite guardar si está vacío
                ) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { taskPendingEdit = null }) { Text("Cancelar") }
            }
        )
    }

    // Modal de Eliminación de Tarea
    taskPendingDelete?.let { task ->
        AlertDialog(
            onDismissRequest = { taskPendingDelete = null },
            title = { Text("Eliminar tarea") },
            text = { Text("¿Seguro que quieres eliminar \"${task.title}\"? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteTask(task)
                    taskPendingDelete = null
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { taskPendingDelete = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun TaskRow(
    task: Task,
    onToggle: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
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
        IconButton(onClick = onEdit) {
            Icon(Icons.Default.Edit, contentDescription = "Editar")
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, contentDescription = "Eliminar")
        }
    }
}