package com.ucb.food.profile.presentation.state

sealed interface ProfileEditEvent {
    data class OnFirstNameChanged(val value: String) : ProfileEditEvent
    data class OnLastNameChanged(val value: String) : ProfileEditEvent
    data class OnAddressChanged(val value: String) : ProfileEditEvent
    data class OnPasswordChanged(val value: String) : ProfileEditEvent
    data object OnSaveClick : ProfileEditEvent
    data object OnBackClick : ProfileEditEvent
}
