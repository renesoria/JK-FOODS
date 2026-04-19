package com.ucb.food.fakestore.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class RatingDto(
    val rate: Double,
    val count: Int
)
