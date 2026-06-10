package com.ucb.food.profile.domain.usecase

import com.ucb.food.login.domain.model.UserProfileModel
import com.ucb.food.login.domain.repository.AuthenticationRepository

class UpdateUserProfileUseCase(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke(profile: UserProfileModel) {
        repository.updateProfile(profile)
    }
}
