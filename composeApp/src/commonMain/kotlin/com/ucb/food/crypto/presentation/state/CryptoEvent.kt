package com.ucb.food.crypto.presentation.state

sealed interface CryptoEvent {
    data object OnLoad : CryptoEvent
    data object OnRefresh : CryptoEvent
}