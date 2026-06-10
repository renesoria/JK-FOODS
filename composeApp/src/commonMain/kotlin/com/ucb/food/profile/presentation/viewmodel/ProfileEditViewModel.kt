package com.ucb.food.profile.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.food.login.domain.model.UserProfileModel
import com.ucb.food.login.domain.repository.AuthenticationRepository
import com.ucb.food.profile.domain.usecase.GetUserProfileUseCase
import com.ucb.food.profile.domain.usecase.UpdateUserProfileUseCase
import com.ucb.food.profile.presentation.state.ProfileEditEvent
import com.ucb.food.profile.presentation.state.ProfileEditUiState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileEditViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase,
    private val authRepository: AuthenticationRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileEditUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<ProfileEditEffect>()
    val effect = _effect.asSharedFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            getUserProfileUseCase().filterNotNull().first().let { u ->
                _state.update {
                    it.copy(
                        firstName = u.firstName,
                        lastName = u.lastName,
                        email = u.email,
                        address = u.address,
                        profilePicture = u.profilePicture
                    )
                }
            }
        }
    }

    fun onEvent(event: ProfileEditEvent) {
        when (event) {
            is ProfileEditEvent.OnFirstNameChanged -> _state.update { it.copy(firstName = event.value) }
            is ProfileEditEvent.OnLastNameChanged -> _state.update { it.copy(lastName = event.value) }
            is ProfileEditEvent.OnProfilePictureChanged -> _state.update { it.copy(profilePicture = event.value) }
            is ProfileEditEvent.OnAddressChanged -> _state.update { it.copy(address = event.value) }
            is ProfileEditEvent.OnPasswordChanged -> _state.update { it.copy(password = event.value) }
            ProfileEditEvent.OnTogglePasswordVisibility -> _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
            ProfileEditEvent.OnSaveClick -> saveChanges()
            ProfileEditEvent.OnBackClick -> viewModelScope.launch { _effect.emit(ProfileEditEffect.NavigateBack) }
        }
    }

    private fun saveChanges() {
        val s = _state.value
        _state.update { it.copy(isLoading = true, error = null) }
        viewModelScope.launch {
            try {
                // 1. Update Password if changed
                if (s.password.isNotBlank() && s.password.length >= 6) {
                    authRepository.updatePassword(s.password)
                }

                // 2. Update Profile info
                val currentUser = getUserProfileUseCase().firstOrNull()
                val profileToSave = currentUser?.copy(
                    firstName = s.firstName,
                    lastName = s.lastName,
                    email = s.email,
                    address = s.address,
                    profilePicture = s.profilePicture
                ) ?: UserProfileModel(
                    firstName = s.firstName,
                    lastName = s.lastName,
                    email = s.email,
                    address = s.address,
                    profilePicture = s.profilePicture
                )

                updateUserProfileUseCase(profileToSave)
                
                _state.update { it.copy(isLoading = false, success = true) }
                _effect.emit(ProfileEditEffect.NavigateBack)
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                val errorMsg = when {
                    e.message?.contains("recent authentication") == true -> 
                        "Por seguridad, debes cerrar sesión y volver a entrar para cambiar tu contraseña."
                    else -> e.message ?: "Error al guardar cambios"
                }
                _state.update { it.copy(error = errorMsg) }
            }
        }
    }
}

sealed interface ProfileEditEffect {
    data object NavigateBack : ProfileEditEffect
}
