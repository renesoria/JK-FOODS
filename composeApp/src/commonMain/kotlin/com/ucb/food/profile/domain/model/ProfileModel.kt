package com.ucb.food.profile.domain.model

data class ProfileModel(
    val id: String,
    val name: String,
    val email: String,
    val cellphone: String,
    val description: String,
    val pathUrl: String
)
