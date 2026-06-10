package com.ucb.food.login.domain.usecase

import com.ucb.food.login.domain.model.LoginModel
import com.ucb.food.login.domain.model.UserProfileModel
import com.ucb.food.login.domain.repository.AuthenticationRepository

class DoSignUpUseCase(
    private val repository: AuthenticationRepository
) {
    suspend fun invoke(model: LoginModel, profile: UserProfileModel) {
        repository.signUp(model, profile)
    }
}