package com.ucb.food.github.data.mapper

import com.ucb.food.github.data.dto.UserDto
import com.ucb.food.github.domain.model.GithubModel

fun UserDto.toModel() = GithubModel(
    name = name?:"",
    urlImage=avatarUrl,
    avatar = login,
    bio = bio ?: "",
    company = company ?: ""
)