package com.ucb.food.profile.domain.usecase

import com.ucb.food.login.domain.repository.AuthenticationRepository

class SyncProfileUseCase(
    private val repository: AuthenticationRepository
) {
    suspend operator fun invoke() {
        repository.syncProfile()
    }
}
