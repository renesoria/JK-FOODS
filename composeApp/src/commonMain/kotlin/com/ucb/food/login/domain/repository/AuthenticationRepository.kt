package com.ucb.food.login.domain.repository

import com.ucb.food.login.domain.model.LoginModel
import com.ucb.food.login.domain.model.UserProfileModel
import kotlinx.coroutines.flow.Flow

interface AuthenticationRepository {
    suspend fun login(model: LoginModel)
    suspend fun signUp(model: LoginModel, profile: UserProfileModel)
    suspend fun updateProfile(profile: UserProfileModel)
    suspend fun syncProfile()
    fun getUserProfile(): Flow<UserProfileModel?>
}