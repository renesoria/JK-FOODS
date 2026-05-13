package com.ucb.food.region.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ucb.food.region.presentation.viewmodel.RegionViewModel

@Composable
fun RegionScreen(
    viewModel: RegionViewModel
) {
    val state by viewModel.state.collectAsState()

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Regiones Activas (Remote Config + RTDB)",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            Button(
                onClick = { viewModel.syncRegions() },
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
                enabled = !state.isLoading
            ) {
                Text("Sincronizar Regiones")
            }

            if (state.isLoading) {
                CircularProgressIndicator()
            }

            state.error?.let {
                Text(text = "Error: $it", color = MaterialTheme.colorScheme.error)
            }

            LazyColumn {
                items(state.regions) { region ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = region.name, style = MaterialTheme.typography.titleMedium)
                            Text(text = "ID: ${region.id}", style = MaterialTheme.typography.bodySmall)
                            Text(text = region.description, style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            }
        }
    }
}
