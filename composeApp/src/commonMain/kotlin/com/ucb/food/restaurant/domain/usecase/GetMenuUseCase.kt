package com.ucb.food.restaurant.domain.usecase

import com.ucb.food.restaurant.domain.model.DishModel
import com.ucb.food.restaurant.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow

class GetMenuUseCase(
    private val repository: RestaurantRepository
) {
    operator fun invoke(restaurantId: String): Flow<List<DishModel>> = repository.getMenu(restaurantId)
}
