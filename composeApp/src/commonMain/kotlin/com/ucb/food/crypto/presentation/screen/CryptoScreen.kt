package com.ucb.food.crypto.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.ucb.food.crypto.presentation.composable.CryptoContent
import com.ucb.food.crypto.presentation.state.CryptoEffect
import com.ucb.food.crypto.presentation.state.CryptoEvent
import com.ucb.food.crypto.presentation.viewmodel.CryptoViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CryptoScreen(
    viewModel: CryptoViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onEvent(CryptoEvent.OnLoad)
    }

    LaunchedEffect(Unit) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is CryptoEffect.ShowError -> {
                    //show error
                }
            }
        }
    }

    when {
        state.isLoading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        state.cryptos.isNotEmpty() -> {
            CryptoContent(state.cryptos)
        }

        state.error != null -> {
            Text("Error: ${state.error}")
        }
    }
}