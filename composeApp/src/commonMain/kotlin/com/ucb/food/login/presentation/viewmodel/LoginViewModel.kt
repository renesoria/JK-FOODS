package com.ucb.food.login.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.food.login.presentation.state.LoginEffect
import com.ucb.food.login.presentation.state.LoginEvent
import com.ucb.food.login.presentation.state.LoginState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginModuleViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _effect = Channel<LoginEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: LoginEvent) {
        when (event) {
            is LoginEvent.OnMobileNumberChange -> {
                _state.update { it.copy(mobileNumber = event.mobileNumber) }
            }
            is LoginEvent.OnPasswordChange -> {
                _state.update { it.copy(password = event.password) }
            }
            LoginEvent.OnLoginClick -> {
                // Simple login logic for now
                _state.update { it.copy(isLoading = true) }
                viewModelScope.launch {
                    // simulate login
                    kotlinx.coroutines.delay(1000)
                    _state.update { it.copy(isLoading = false) }
                    _effect.send(LoginEffect.LoginSuccess)
                }
            }
            LoginEvent.OnSignUpClick -> {
                viewModelScope.launch {
                    _effect.send(LoginEffect.NavigateToSignUp)
                }
            }
            LoginEvent.OnBackClick -> {
                viewModelScope.launch {
                    _effect.send(LoginEffect.NavigateBack)
                }
            }
        }
    }
}