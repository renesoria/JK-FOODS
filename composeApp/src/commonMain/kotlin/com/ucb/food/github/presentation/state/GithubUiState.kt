package com.ucb.food.github.presentation.state

import com.ucb.food.github.domain.model.GithubModel

data class GithubUiState(
    val isLoading: Boolean = false,
    val nickname: String = "",
    val model: GithubModel = GithubModel()
)
