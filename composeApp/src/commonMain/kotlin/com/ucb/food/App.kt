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
import com.example.designsystem.theme.AppTheme
import com.ucb.food.navigation.AppNavHost
import com.ucb.food.core.presentation.viewmodel.ThemeViewModel
import org.koin.compose.viewmodel.koinViewModel
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme


@Composable
@Preview
fun App(
    themeViewModel: ThemeViewModel = koinViewModel()
) {
    val isDarkMode by themeViewModel.isDarkMode.collectAsState()
    val currentMode = if (isDarkMode) ThemeMode.DARK else ThemeMode.LIGHT
    val snackbarHostState = remember { SnackbarHostState() }
    
    DsTheme(
        mode = currentMode
    ) {
        val colors = AppTheme.colors
        val colorScheme = if (isDarkMode) {
            darkColorScheme(
                primary = colors.primary,
                background = colors.background,
                surface = colors.surface,
                onBackground = colors.textPrimary,
                onSurface = colors.textPrimary
            )
        } else {
            lightColorScheme(
                primary = colors.primary,
                background = colors.background,
                surface = colors.surface,
                onBackground = colors.textPrimary,
                onSurface = colors.textPrimary
            )
        }

        MaterialTheme(
            colorScheme = colorScheme
        ) {
            Scaffold(
                contentWindowInsets = WindowInsets.safeDrawing,
                snackbarHost = { SnackbarHost(snackbarHostState) },
                containerColor = colors.surface 
            ) { paddingValues ->
                AppNavHost()
            }
        }
    }
}
