package com.ucb.food.profile.presentation.state

sealed interface ProfileEffect {
    data object NavigateToEditProfile : ProfileEffect
    data object NavigateToMyReviews : ProfileEffect
    data object NavigateToLogin : ProfileEffect
    data object NavigateBack : ProfileEffect
    data class ShowError(val message: String) : ProfileEffect
}
