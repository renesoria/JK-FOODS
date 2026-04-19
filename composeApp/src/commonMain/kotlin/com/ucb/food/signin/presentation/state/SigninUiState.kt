package com.ucb.food.signin.presentation.state

data class SigninUiState(
    val isLoading: Boolean = false,
    val email: String = "",
    val password : String = ""
)
