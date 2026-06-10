package com.ucb.food.login.data.repository

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val userId: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val mobileNumber: String,
    val gender: String,
    val address: String,
    val profilePicture: String? = null
)
