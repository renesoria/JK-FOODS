package com.ucb.food.signin.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.food.login.domain.model.LoginModel
import com.ucb.food.login.domain.model.UserProfileModel
import com.ucb.food.login.domain.usecase.DoSignUpUseCase
import com.ucb.food.signin.presentation.state.SigninEffect
import com.ucb.food.signin.presentation.state.SigninEvent
import com.ucb.food.signin.presentation.state.SigninUiState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SigninViewModel(
    private val doSignUpUseCase: DoSignUpUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SigninUiState())
    val state = _state.asStateFlow()

    private val _effect = MutableSharedFlow<SigninEffect>()
    val effect = _effect.asSharedFlow()

    fun onEvent(event: SigninEvent) {
        when (event) {
            is SigninEvent.OnFirstNameChanged -> {
                val filtered = event.value.filter { it.isLetter() || it.isWhitespace() }
                _state.update { it.copy(firstName = filtered, firstNameError = null) }
            }
            is SigninEvent.OnLastNameChanged -> {
                val filtered = event.value.filter { it.isLetter() || it.isWhitespace() }
                _state.update { it.copy(lastName = filtered, lastNameError = null) }
            }
            is SigninEvent.OnMobileNumberChanged -> {
                val filtered = event.value.filter { it.isDigit() }
                _state.update { it.copy(mobileNumber = filtered, mobileError = null) }
            }
            is SigninEvent.OnEmailChanged -> _state.update { it.copy(email = event.value, emailError = null) }
            is SigninEvent.OnGenderChanged -> _state.update { it.copy(gender = event.value) }
            is SigninEvent.OnPasswordChanged -> _state.update { it.copy(password = event.value, passwordError = null) }
            is SigninEvent.OnAddressChanged -> _state.update { it.copy(address = event.value, addressError = null) }
            SigninEvent.OnSignUpClick -> {
                signUp()
            }
            SigninEvent.OnLoginClick -> {
                viewModelScope.launch { _effect.emit(SigninEffect.NavigateToLogin) }
            }
            SigninEvent.OnBackClick -> {
                viewModelScope.launch { _effect.emit(SigninEffect.NavigateBack) }
            }
        }
    }

    private fun signUp() {
        val s = _state.value
        var hasError = false

        if (s.firstName.isBlank()) {
            _state.update { it.copy(firstNameError = "Obligatorio") }
            hasError = true
        }
        if (s.lastName.isBlank()) {
            _state.update { it.copy(lastNameError = "Obligatorio") }
            hasError = true
        }
        if (s.mobileNumber.isBlank()) {
            _state.update { it.copy(mobileError = "Obligatorio") }
            hasError = true
        }
        if (s.email.isBlank()) {
            _state.update { it.copy(emailError = "Obligatorio") }
            hasError = true
        } else if (!s.email.contains("@")) {
            _state.update { it.copy(emailError = "Email inválido") }
            hasError = true
        }
        if (s.password.length < 6) {
            _state.update { it.copy(passwordError = "Mínimo 6 caracteres") }
            hasError = true
        }
        if (s.address.isBlank()) {
            _state.update { it.copy(addressError = "Obligatorio") }
            hasError = true
        }

        if (hasError) return

        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val model = LoginModel(s.email, s.password)
                val profile = UserProfileModel(
                    firstName = s.firstName,
                    lastName = s.lastName,
                    email = s.email,
                    mobileNumber = s.mobileNumber,
                    gender = s.gender,
                    address = s.address
                )
                doSignUpUseCase.invoke(model, profile)
                _state.update { it.copy(isLoading = false) }
                _effect.emit(SigninEffect.SignUpSuccess)
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false) }
                val msg = when {
                    e.message?.contains("already in use") == true -> "Correo ya registrado"
                    else -> e.message ?: "Error al registrarse"
                }
                _effect.emit(SigninEffect.ShowError(msg))
            }
        }
    }
}