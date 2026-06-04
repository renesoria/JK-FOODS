package com.ucb.food.profile.presentation.state

sealed interface ProfileEvent {
    data object OnEditProfileClick : ProfileEvent
    data object OnLogoutClick : ProfileEvent
    data object OnBackClick : ProfileEvent
    data object OnMyReviewsClick : ProfileEvent
    data object OnNotificationsClick : ProfileEvent
    data object OnAboutClick : ProfileEvent
}
