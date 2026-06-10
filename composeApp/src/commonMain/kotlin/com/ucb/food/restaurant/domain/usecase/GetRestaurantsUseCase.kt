package com.ucb.food.restaurant.domain.usecase

import com.ucb.food.restaurant.domain.model.RestaurantModel
import com.ucb.food.restaurant.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow

class GetRestaurantsUseCase(
    private val repository: RestaurantRepository
) {
    operator fun invoke(): Flow<List<RestaurantModel>> = repository.getRestaurants()
}
