package com.ucb.food.nm.login.presentation.state

sealed interface LoginEvent {
    data class OnEmailChanged(
        val email: String
    ): LoginEvent

    data class OnPasswordChanged(
        val password: String
    ): LoginEvent

    object OnSignInClicked: LoginEvent
}