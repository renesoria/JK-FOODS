package com.ucb.food.github.domain.repository

import com.ucb.food.github.domain.model.GithubModel

interface GithubRepository {
    suspend fun obtainAvatar(
        avatar: String
    ): GithubModel
}