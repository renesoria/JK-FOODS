package com.ucb.food.profile.presentation.state

import com.ucb.food.login.domain.model.UserProfileModel

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: UserProfileModel? = null,
    val error: String? = null
)
