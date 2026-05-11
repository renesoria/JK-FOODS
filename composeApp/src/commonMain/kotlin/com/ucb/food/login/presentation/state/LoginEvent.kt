package com.ucb.food.login.presentation.state

sealed interface LoginEvent {
    data class OnMobileNumberChange(val mobileNumber: String) : LoginEvent
    data class OnPasswordChange(val password: String) : LoginEvent
    data object OnLoginClick : LoginEvent
    data object OnSignUpClick : LoginEvent
    data object OnBackClick : LoginEvent
}