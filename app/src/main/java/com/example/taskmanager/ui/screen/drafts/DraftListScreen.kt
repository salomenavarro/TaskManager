package com.example.taskmanager.ui.screen.drafts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
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
import com.example.taskmanager.domain.model.TaskDraft

@Composable
fun DraftListScreen(
    viewModel: DraftListViewModel = hiltViewModel(),
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    var draftPendingDelete by remember { mutableStateOf<TaskDraft?>(null) }
    var draftPendingEdit by remember { mutableStateOf<TaskDraft?>(null) } // <-- Estado de edición

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onBack) { Text("← Volver") }
        }
        Text("Borradores", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = title, onValueChange = { title = it },
            label = { Text("Título") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = description, onValueChange = { description = it },
            label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                viewModel.saveDraft(title, description)
                title = ""; description = ""
            },
            enabled = title.isNotBlank(), // Deshabilitado si solo hay espacios
            modifier = Modifier.fillMaxWidth()
        ) { Text("Guardar borrador") }

        Spacer(Modifier.height(16.dp))

        if (uiState.publishError != null) {
            Text(
                "No se pudo publicar: ${uiState.publishError}. El borrador se conservó.",
                color = MaterialTheme.colorScheme.error
            )
            TextButton(onClick = { viewModel.dismissPublishError() }) { Text("Cerrar") }
        }

        when {
            uiState.isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) { CircularProgressIndicator() }
            uiState.drafts.isEmpty() -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                Text("No tienes borradores guardados")
            }
            else -> LazyColumn {
                items(uiState.drafts, key = { it.id }) { draft ->
                    DraftRow(
                        draft = draft,
                        isPublishing = uiState.publishingDraftId == draft.id,
                        onPublish = { viewModel.publishDraft(draft) },
                        onEdit = { draftPendingEdit = draft },
                        onDelete = { draftPendingDelete = draft }
                    )
                }
            }
        }
    }

    // Modal de Edición de Borrador
    draftPendingEdit?.let { draft ->
        var editTitle by remember { mutableStateOf(draft.title) }
        var editDescription by remember { mutableStateOf(draft.description) }

        AlertDialog(
            onDismissRequest = { draftPendingEdit = null },
            title = { Text("Editar borrador") },
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
                        viewModel.updateDraft(draft, editTitle, editDescription)
                        draftPendingEdit = null
                    },
                    enabled = editTitle.isNotBlank() // No permite guardar espacios en blanco
                ) { Text("Guardar") }
            },
            dismissButton = {
                TextButton(onClick = { draftPendingEdit = null }) { Text("Cancelar") }
            }
        )
    }

    // Modal de Eliminación
    draftPendingDelete?.let { draft ->
        AlertDialog(
            onDismissRequest = { draftPendingDelete = null },
            title = { Text("Eliminar borrador") },
            text = { Text("¿Seguro que quieres eliminar \"${draft.title}\"? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteDraft(draft)
                    draftPendingDelete = null
                }) { Text("Eliminar") }
            },
            dismissButton = {
                TextButton(onClick = { draftPendingDelete = null }) { Text("Cancelar") }
            }
        )
    }
}

@Composable
fun DraftRow(
    draft: TaskDraft,
    isPublishing: Boolean,
    onPublish: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text(draft.title, style = MaterialTheme.typography.bodyLarge)
            if (draft.description.isNotBlank()) Text(draft.description, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(
                    onClick = onPublish,
                    enabled = !isPublishing
                ) {
                    if (isPublishing) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            color = MaterialTheme.colorScheme.onPrimary,
                            strokeWidth = 2.dp
                        )
                        Spacer(Modifier.width(8.dp))
                        Text("Publicando...")
                    } else {
                        Text("Publicar")
                    }
                }
                Spacer(Modifier.width(8.dp))
                OutlinedButton(onClick = onEdit, enabled = !isPublishing) { Text("Editar") }
                Spacer(Modifier.width(8.dp))
                OutlinedButton(onClick = onDelete, enabled = !isPublishing) { Text("Eliminar") }
            }
        }
    }
}