package com.ucb.food.profile.presentation.state

data class ProfileEditUiState(
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val address: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val success: Boolean = false,
    val error: String? = null
)
