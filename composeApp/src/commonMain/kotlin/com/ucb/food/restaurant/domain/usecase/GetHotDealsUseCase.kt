package com.ucb.food.restaurant.domain.usecase

import com.ucb.food.restaurant.domain.model.HotDealModel
import com.ucb.food.restaurant.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow

class GetHotDealsUseCase(
    private val repository: RestaurantRepository
) {
    operator fun invoke(): Flow<List<HotDealModel>> = repository.getHotDeals()
}
