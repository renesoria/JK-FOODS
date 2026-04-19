package com.ucb.food.github.data.datasource

import com.ucb.food.github.data.dto.UserDto

interface GithubRemoteDataSource {
    suspend fun getUser(nickname: String): UserDto
}