package com.ucb.food.profile.domain.usecase

import com.ucb.food.profile.domain.model.ProfileModel
import com.ucb.food.profile.domain.repository.ProfileRepository

class SaveProfileUseCase(
    private val repository: ProfileRepository
) {
    suspend fun invoke(profile: ProfileModel) {
        repository.create(profile)
    }
}