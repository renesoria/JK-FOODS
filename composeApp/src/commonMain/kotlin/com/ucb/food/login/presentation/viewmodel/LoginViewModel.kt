package com.ucb.food.login.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.food.login.domain.model.LoginModel
import com.ucb.food.login.domain.usecase.DoLoginUseCase
import com.ucb.food.login.presentation.state.LoginEffect
import com.ucb.food.login.presentation.state.LoginEvent
import com.ucb.food.login.presentation.state.LoginState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginModuleViewModel(
    private val doLoginUseCase: DoLoginUseCase
) : ViewModel() {
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
                login()
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

    private fun login() {
        val email = _state.value.mobileNumber // The field is named mobileNumber in UI but email in domain
        val password = _state.value.password

        if (email.isBlank() || password.isBlank()) {
            viewModelScope.launch {
                _effect.send(LoginEffect.ShowError("Please fill in all fields"))
            }
            return
        }

        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                doLoginUseCase.invoke(LoginModel(email, password))
                _state.update { it.copy(isLoading = false) }
                _effect.send(LoginEffect.LoginSuccess)
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                val errorMessage = when {
                    e.message?.contains("invalid-credential") == true || e.message?.contains("user-not-found") == true -> "Correo o contraseña incorrectos"
                    else -> e.message ?: "Error al iniciar sesión"
                }
                _effect.send(LoginEffect.ShowError(errorMessage))
            }
        }
    }
}