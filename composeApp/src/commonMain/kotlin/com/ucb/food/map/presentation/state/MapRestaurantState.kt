package com.ucb.food.map.presentation.state

import kotlinx.serialization.Serializable
import com.ucb.food.restaurant.domain.model.RestaurantModel
import com.ucb.food.restaurant.domain.model.BranchModel

@Serializable
data class MapRestaurantState(
    val id: String,
    val name: String,
    val logoUrl: String,
    val overallRating: Double,
    val branches: List<MapBranchState>
)

@Serializable
data class MapBranchState(
    val id: String,
    val address: String,
    val latitude: Double,
    val longitude: Double
)

fun RestaurantModel.toMapState() = MapRestaurantState(
    id = id,
    name = name,
    logoUrl = logoUrl,
    overallRating = overallRating,
    branches = branches.map { it.toMapState() }
)

fun BranchModel.toMapState() = MapBranchState(
    id = id,
    address = address,
    latitude = latitude,
    longitude = longitude
)
