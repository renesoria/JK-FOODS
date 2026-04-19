package com.ucb.food.profile.domain.usecase

import com.ucb.food.profile.domain.model.ProfileModel
import com.ucb.food.profile.domain.repository.ProfileRepository

class EditProfileUseCase(
    val repository: ProfileRepository
) {
    suspend fun invoke(model: ProfileModel) {
        repository.update(model)
    }
}