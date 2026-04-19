package com.ucb.food.login.domain.repository

import com.ucb.food.login.domain.model.LoginModel

interface AuthenticationRepository {
    suspend fun login(model: LoginModel)
}