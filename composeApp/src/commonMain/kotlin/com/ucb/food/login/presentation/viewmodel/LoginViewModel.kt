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
            is LoginEvent.OnEmailChange -> {
                _state.update { it.copy(email = event.email, emailError = null) }
            }
            is LoginEvent.OnPasswordChange -> {
                _state.update { it.copy(password = event.password, passwordError = null) }
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
        val email = _state.value.email
        val password = _state.value.password

        var hasError = false
        if (email.isBlank()) {
            _state.update { it.copy(emailError = "El correo es obligatorio") }
            hasError = true
        } else if (!email.contains("@")) {
            _state.update { it.copy(emailError = "Formato de correo inválido") }
            hasError = true
        }

        if (password.isBlank()) {
            _state.update { it.copy(passwordError = "La contraseña es obligatoria") }
            hasError = true
        }

        if (hasError) return

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
                    else -> "Error al iniciar sesión"
                }
                _effect.send(LoginEffect.ShowError(errorMessage))
            }
        }
    }
}