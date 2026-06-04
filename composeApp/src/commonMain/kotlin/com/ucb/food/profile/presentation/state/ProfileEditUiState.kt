package com.ucb.food.profile.presentation.state

data class ProfileEditUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val address: String = "",
    val profilePicture: String? = null,
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)
