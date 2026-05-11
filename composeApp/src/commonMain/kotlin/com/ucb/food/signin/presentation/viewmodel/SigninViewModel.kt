package com.ucb.food.signin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.food.signin.presentation.state.SigninEffect
import com.ucb.food.signin.presentation.state.SigninEvent
import com.ucb.food.signin.presentation.state.SigninUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SigninViewModel : ViewModel() {

    private val _state = MutableStateFlow(SigninUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SigninEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: SigninEvent) {
        when (event) {
            is SigninEvent.OnFirstNameChanged -> _state.update { it.copy(firstName = event.value) }
            is SigninEvent.OnLastNameChanged -> _state.update { it.copy(lastName = event.value) }
            is SigninEvent.OnMobileNumberChanged -> _state.update { it.copy(mobileNumber = event.value) }
            is SigninEvent.OnEmailChanged -> _state.update { it.copy(email = event.value) }
            is SigninEvent.OnGenderChanged -> _state.update { it.copy(gender = event.value) }
            is SigninEvent.OnPasswordChanged -> _state.update { it.copy(password = event.value) }
            is SigninEvent.OnAddressChanged -> _state.update { it.copy(address = event.value) }
            SigninEvent.OnSignUpClick -> {
                _state.update { it.copy(isLoading = true) }
                viewModelScope.launch {
                    kotlinx.coroutines.delay(1000)
                    _state.update { it.copy(isLoading = false) }
                    _effect.emit(SigninEffect.SignUpSuccess)
                }
            }
            SigninEvent.OnLoginClick -> {
                viewModelScope.launch { _effect.emit(SigninEffect.NavigateToLogin) }
            }
            SigninEvent.OnBackClick -> {
                viewModelScope.launch { _effect.emit(SigninEffect.NavigateBack) }
            }
        }
    }
}