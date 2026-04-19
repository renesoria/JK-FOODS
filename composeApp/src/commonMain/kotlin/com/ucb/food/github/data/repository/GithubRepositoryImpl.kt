package com.ucb.food.github.data.repository

import com.ucb.food.github.data.datasource.GithubRemoteDataSource
import com.ucb.food.github.data.mapper.toModel
import com.ucb.food.github.domain.model.GithubModel
import com.ucb.food.github.domain.repository.GithubRepository

class GithubRepositoryImpl(
    val remote: GithubRemoteDataSource
): GithubRepository {
    override suspend fun obtainAvatar(avatar: String): GithubModel {
        val response = remote.getUser(avatar)
        return response.toModel()
    }
}