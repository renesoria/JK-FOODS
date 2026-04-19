package com.ucb.food.login.domain.usecase

import com.ucb.food.login.domain.model.LoginModel
import com.ucb.food.login.domain.repository.AuthenticationRepository

class DoLoginUseCase(
    val repository: AuthenticationRepository
) {

    suspend fun invoke(model: LoginModel) {
        repository.login(model)
    }
}