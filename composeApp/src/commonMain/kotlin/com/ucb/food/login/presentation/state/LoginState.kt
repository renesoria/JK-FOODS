package com.ucb.food.login.presentation.state

data class LoginState(
    val mobileNumber: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)