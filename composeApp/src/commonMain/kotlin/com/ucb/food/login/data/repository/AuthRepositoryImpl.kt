package com.ucb.food.login.data.repository

import com.ucb.food.login.data.service.FirebaseAuthService
import com.ucb.food.login.domain.model.LoginModel
import com.ucb.food.login.domain.repository.AuthenticationRepository

class AuthRepositoryImpl(
    private val firebaseAuthService: FirebaseAuthService
) : AuthenticationRepository {
    override suspend fun login(model: LoginModel) {
        firebaseAuthService.login(model)
    }

    override suspend fun signUp(model: LoginModel) {
        firebaseAuthService.signUp(model)
    }
}