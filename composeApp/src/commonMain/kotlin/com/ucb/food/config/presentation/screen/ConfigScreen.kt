package com.ucb.food.config.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ucb.food.config.presentation.viewmodel.ConfigViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ConfigScreen(
    viewModel: ConfigViewModel = koinViewModel()
) {
    val config by viewModel.config.collectAsState()

    // Intentamos convertir el string hex a Color, si falla usamos Gris
    val bgColor = remember(config?.backgroundColor) {
        try {
            if (config?.backgroundColor?.startsWith("#") == true) {
                Color(handleColor(config?.backgroundColor ?: "#FFFFFF"))
            } else {
                Color.LightGray
            }
        } catch (e: Exception) {
            Color.LightGray
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(bgColor)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Ejercicio 1: Sync & Cache",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Mensaje desde Room:", fontWeight = FontWeight.Bold)
                    Text(
                        text = config?.welcomeMessage ?: "Cargando o sin datos...",
                        fontSize = 24.sp
                    )
                }
            }

            Button(
                onClick = { viewModel.onSyncClicked() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sincronizar con Firebase")
            }
            
            Text(
                text = "Nota: Si no hay internet, se mantendrá el último mensaje guardado.",
                style = MaterialTheme.typography.bodySmall,
                color = Color.DarkGray
            )
        }
    }
}

// Función auxiliar para parsear colores hexadecimales
fun handleColor(colorString: String): Long {
    val hex = colorString.replace("#", "")
    return if (hex.length == 6) {
        ("FF$hex").toLong(16)
    } else {
        hex.toLong(16)
    }
}
