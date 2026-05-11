package com.ucb.food.signin.presentation.state

data class SigninUiState(
    val isLoading: Boolean = false,
    val firstName: String = "",
    val lastName: String = "",
    val mobileNumber: String = "",
    val email: String = "",
    val gender: String = "",
    val password : String = "",
    val address: String = "",
    val error: String? = null
)