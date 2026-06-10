package com.ucb.food.restaurant.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class PublicNotificationDto(
    val id: String = "",
    val title: String = "",
    val message: String = "",
    val senderId: String = "",
    val timestamp: Long = 0L
)
