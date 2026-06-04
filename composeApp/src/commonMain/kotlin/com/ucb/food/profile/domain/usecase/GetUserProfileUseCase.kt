package com.ucb.food.profile.domain.usecase

import com.ucb.food.login.domain.model.UserProfileModel
import com.ucb.food.login.domain.repository.AuthenticationRepository
import kotlinx.coroutines.flow.Flow

class GetUserProfileUseCase(
    private val repository: AuthenticationRepository
) {
    operator fun invoke(): Flow<UserProfileModel?> {
        return repository.getUserProfile()
    }
}
