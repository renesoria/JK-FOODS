package com.ucb.food.restaurant.domain.repository

import com.ucb.food.restaurant.domain.model.DishModel
import com.ucb.food.restaurant.domain.model.HotDealModel
import com.ucb.food.restaurant.domain.model.RestaurantModel
import com.ucb.food.restaurant.domain.model.ReviewModel
import kotlinx.coroutines.flow.Flow

interface RestaurantRepository {
    fun getRestaurants(): Flow<List<RestaurantModel>>
    fun getRestaurantById(id: String): Flow<RestaurantModel?>
    fun getMenu(restaurantId: String): Flow<List<DishModel>>
    fun getHotDeals(): Flow<List<HotDealModel>>
    fun getReviews(branchId: String): Flow<List<ReviewModel>>
    fun getUserReviews(userId: String): Flow<List<ReviewModel>>
    suspend fun addReview(review: ReviewModel)
    
    // Nueva función para la notificación
    suspend fun sendFiveStarNotification(restaurantName: String, dishName: String)
}
