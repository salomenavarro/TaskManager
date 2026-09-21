package com.example.taskmanager.ui.screen.drafts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.OutlinedButton
import com.example.taskmanager.domain.model.Task
import com.example.taskmanager.domain.model.TaskDraft
import com.example.taskmanager.ui.screen.login.AuthViewModel

// ui/screen/drafts/DraftListScreen.kt
@Composable
fun DraftListScreen(viewModel: DraftListViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
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
                        onPublish = { viewModel.publishDraft(draft) },
                        onDelete = { viewModel.deleteDraft(draft) }
                    )
                }
            }
        }
    }
}

@Composable
fun DraftRow(draft: TaskDraft, onPublish: () -> Unit, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Column(Modifier.padding(12.dp)) {
            Text(draft.title, style = MaterialTheme.typography.bodyLarge)
            if (draft.description.isNotBlank()) Text(draft.description, style = MaterialTheme.typography.bodySmall)
            Spacer(Modifier.height(8.dp))
            Row {
                Button(onClick = onPublish) { Text("Publicar") }
                Spacer(Modifier.width(8.dp))
                OutlinedButton(onClick = onDelete) { Text("Eliminar") }
            }
        }
    }
}