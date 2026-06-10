package com.ucb.food.restaurant.domain.usecase

import com.ucb.food.restaurant.domain.model.RestaurantModel
import com.ucb.food.restaurant.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow

class GetRestaurantDetailsUseCase(
    private val repository: RestaurantRepository
) {
    operator fun invoke(id: String): Flow<RestaurantModel?> = repository.getRestaurantById(id)
}
