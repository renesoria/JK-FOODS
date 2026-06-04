package com.ucb.food.login.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserProfileDto(
    val userId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val mobileNumber: String,
    val gender: String,
    val address: String,
    val profilePicture: String? = null
)
