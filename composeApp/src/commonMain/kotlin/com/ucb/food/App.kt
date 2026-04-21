package com.ucb.food

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.example.designsystem.theme.DsTheme
import com.example.designsystem.theme.ThemeMode
import com.ucb.food.navigation.AppNavHost


@Composable
@Preview
fun App() {
    val currentMode = ThemeMode.HIGH_CONTRAST
    val snackbarHostState = remember { SnackbarHostState() }
    DsTheme(
        mode = currentMode
    ) {
        Scaffold(
            contentWindowInsets = WindowInsets.safeDrawing,
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingVaues ->
            AppNavHost()
        }


    }
}
