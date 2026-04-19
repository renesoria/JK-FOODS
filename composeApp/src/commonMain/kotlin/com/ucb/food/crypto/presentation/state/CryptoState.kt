package com.ucb.food.crypto.presentation.state

import com.ucb.food.crypto.domain.model.CryptoModel

data class CryptoState(val isLoading: Boolean = false,
                       val cryptos: List<CryptoModel> = emptyList(),
                       val error: String? = null
)