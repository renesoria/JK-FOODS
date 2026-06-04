package com.ucb.food.restaurant.domain.repository

import com.ucb.food.restaurant.domain.model.DishModel
import com.ucb.food.restaurant.domain.model.RestaurantModel
import com.ucb.food.restaurant.domain.model.ReviewModel
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {
    fun getRestaurants(): Flow<List<RestaurantModel>>
    fun getRestaurantDetails(id: String): Flow<RestaurantModel?>
    fun getMenu(restaurantId: String): Flow<List<DishModel>>
    fun getReviews(branchId: String): Flow<List<ReviewModel>>
    suspend fun addReview(review: ReviewModel)
}
