package com.ucb.food.login.domain.model

data class UserProfileModel(
    val userId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val mobileNumber: String = "",
    val gender: String = "",
    val address: String = ""
)