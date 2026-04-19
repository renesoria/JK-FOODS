package com.ucb.food.github.domain.usecase

import com.ucb.food.github.domain.model.GithubModel
import com.ucb.food.github.domain.repository.GithubRepository

class GetAvatarUseCase(
    val repository: GithubRepository
) {
    suspend fun invoke(
        model: GithubModel
    ): GithubModel {
        return repository.obtainAvatar(
            model.avatar
        )
    }
}