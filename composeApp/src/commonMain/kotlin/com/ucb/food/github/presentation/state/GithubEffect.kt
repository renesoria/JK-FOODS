package com.ucb.food.github.presentation.state

sealed interface GithubEffect {
    data class ShowError(val value: String): GithubEffect
}