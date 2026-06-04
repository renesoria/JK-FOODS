package com.ucb.food.restaurant.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class ReviewDto(
    val id: String = "",
    val userId: String = "",
    val userName: String = "",
    val userProfilePicture: String? = null,
    val restaurantId: String = "",
    val branchId: String = "",
    val rating: Int = 0,
    val comment: String = "",
    val photoUrl: String? = null,
    val consumedDishes: List<String> = emptyList(),
    
    // Checklist attributes
    val hasParking: Boolean = false,
    val hasWifi: Boolean = false,
    val acceptsDigitalPayment: Boolean = false,
    val isPetFriendly: Boolean = false,
    val hasKidsArea: Boolean = false,
    
    // Quick tags
    val isFastService: Boolean = false,
    val isSlowService: Boolean = false,
    val isClean: Boolean = false,
    val isCozy: Boolean = false,
    val isLoud: Boolean = false,
    
    // Value for money
    val isFairPrice: Boolean = false,
    val isExpensive: Boolean = false,
    
    val timestamp: Long = 0L
)
