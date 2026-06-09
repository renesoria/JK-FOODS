package com.ucb.food.profile.presentation.state

import com.ucb.food.login.domain.model.UserProfileModel
import com.ucb.food.restaurant.domain.model.ReviewModel

data class ProfileUiState(
    val isLoading: Boolean = false,
    val user: UserProfileModel? = null,
    val reviewCount: Int = 0,
    val reviews: List<ReviewModel> = emptyList(),
    val isDarkMode: Boolean = false,
    val error: String? = null
)
