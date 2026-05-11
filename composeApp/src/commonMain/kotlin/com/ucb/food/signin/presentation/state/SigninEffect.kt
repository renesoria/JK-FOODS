package com.ucb.food.signin.presentation.state

sealed interface SigninEffect {
    data class ShowError(val message: String) : SigninEffect
    data object NavigateToLogin : SigninEffect
    data object NavigateBack : SigninEffect
    data object SignUpSuccess : SigninEffect
}