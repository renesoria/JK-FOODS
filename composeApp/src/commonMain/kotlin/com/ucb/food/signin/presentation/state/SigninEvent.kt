package com.ucb.food.signin.presentation.state

sealed interface SigninEvent {
    data class OnFirstNameChanged(val value: String) : SigninEvent
    data class OnLastNameChanged(val value: String) : SigninEvent
    data class OnMobileNumberChanged(val value: String) : SigninEvent
    data class OnEmailChanged(val value: String) : SigninEvent
    data class OnGenderChanged(val value: String) : SigninEvent
    data class OnPasswordChanged(val value: String) : SigninEvent
    data class OnAddressChanged(val value: String) : SigninEvent
    data object OnSignUpClick : SigninEvent
    data object OnLoginClick : SigninEvent
    data object OnBackClick : SigninEvent
}