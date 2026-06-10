package com.ucb.food.firebase

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun FirebaseTestScreen(
    viewModel: FirebaseTestViewModel = koinViewModel()
) {
    var textToSave by remember { mutableStateOf("") }
    val localTodos by viewModel.localTodos.collectAsState()
    val cachedConfig by viewModel.cachedConfig.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 32.dp)
    ) {
        Text("Pruebas Firebase", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
             colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("Sincronización & Caché Local", style = MaterialTheme.typography.titleMedium)
                Text("Valor en Room (Caché):", style = MaterialTheme.typography.labelLarge)
                Text(cachedConfig, style = MaterialTheme.typography.bodyLarge, color = Color.Blue)
                Text("Si apagas el internet, este valor persistirá.", style = MaterialTheme.typography.bodySmall)
            }
        }

        // Remote Config (Directo)
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("Remote Config (Nube Directa)", style = MaterialTheme.typography.titleMedium)
                Text("Valor actual: ${viewModel.remoteConfigText}")
            }
        }

        // Realtime Database
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Column(modifier = Modifier.padding(8.dp)) {
                Text("Realtime Database", style = MaterialTheme.typography.titleMedium)
                TextField(
                    value = textToSave,
                    onValueChange = { textToSave = it },
                    label = { Text("Mensaje a guardar") },
                    modifier = Modifier.fillMaxWidth()
                )
                Button(onClick = { viewModel.testRealtimeDatabase(textToSave) }) {
                    Text("Guardar en Firebase")
                }
                Text("Estado: ${viewModel.realtimeDbStatus}")
            }
        }

        // Local Database (Room)
        Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
            Column(modifier = Modifier.padding(8.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Database Local (Room)", style = MaterialTheme.typography.titleMedium)
                    TextButton(onClick = { viewModel.clearAllTodos() }) {
                        Text("Limpiar Todo", color = Color.Red)
                    }
                }
                
                Button(
                    onClick = { viewModel.addLocalTodo("Tarea ${localTodos.size + 1}") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Agregar Item Local")
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                LazyColumn(modifier = Modifier.heightIn(max = 200.dp)) {
                    items(localTodos) { todo ->
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("- ${todo.title}")
                            TextButton(onClick = { viewModel.deleteTodo(todo) }) {
                                Text("Borrar", color = Color.Gray, style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
