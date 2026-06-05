package com.ucb.food.restaurant.data.repository

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurants")
data class RestaurantEntity(
    @PrimaryKey val id: String,
    val name: String,
    val logoUrl: String,
    val overallRating: Double,
    val description: String,
    val branchesJson: String // Guardamos las sucursales como texto JSON por simplicidad
)

@Entity(tableName = "dishes")
data class DishEntity(
    @PrimaryKey val id: String,
    val restaurantId: String,
    val name: String,
    val price: Double,
    val category: String,
    val imageUrl: String?
)
