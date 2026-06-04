package com.ucb.food.login.data.mapper

import com.ucb.food.login.data.dto.UserProfileDto
import com.ucb.food.login.data.repository.UserEntity
import com.ucb.food.login.domain.model.UserProfileModel

fun UserProfileModel.toDto() = UserProfileDto(
    userId = userId,
    firstName = firstName,
    lastName = lastName,
    email = email,
    mobileNumber = mobileNumber,
    gender = gender,
    address = address
)

fun UserProfileDto.toModel() = UserProfileModel(
    userId = userId,
    firstName = firstName,
    lastName = lastName,
    email = email,
    mobileNumber = mobileNumber,
    gender = gender,
    address = address
)

fun UserProfileDto.toEntity() = UserEntity(
    userId = userId,
    firstName = firstName,
    lastName = lastName,
    email = email,
    mobileNumber = mobileNumber,
    gender = gender,
    address = address
)
