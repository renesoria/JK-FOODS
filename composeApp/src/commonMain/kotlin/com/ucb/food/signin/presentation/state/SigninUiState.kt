package com.ucb.food.signin.presentation.state

data class SigninUiState(
    val firstName: String = "",
    val lastName: String = "",
    val mobileNumber: String = "",
    val email: String = "",
    val gender: String = "",
    val password: String = "",
    val address: String = "",
    val isLoading: Boolean = false,
    val firstNameError: String? = null,
    val lastNameError: String? = null,
    val mobileError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val addressError: String? = null
)