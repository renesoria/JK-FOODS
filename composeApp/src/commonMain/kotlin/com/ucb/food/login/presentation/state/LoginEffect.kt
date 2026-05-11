package com.ucb.food.login.presentation.state

sealed interface LoginEffect {
    data object NavigateToSignUp : LoginEffect
    data object NavigateBack : LoginEffect
    data object LoginSuccess : LoginEffect
    data class ShowError(val message: String) : LoginEffect
}