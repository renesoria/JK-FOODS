package com.ucb.food.login.presentation.state

sealed interface LoginEffect {
    object NavigateToHome: LoginEffect
    data class ShowError(
        val message: String
    ): LoginEffect
}