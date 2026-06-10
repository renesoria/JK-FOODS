package com.ucb.food.map.presentation.screen

import androidx.compose.runtime.Composable
import com.ucb.food.map.presentation.viewmodel.MapViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
expect fun MapScreen(
    viewModel: MapViewModel = koinViewModel(),
    onNavigateBack: () -> Unit
)
