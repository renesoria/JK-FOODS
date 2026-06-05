package com.ucb.food.restaurant.domain.usecase

import com.ucb.food.restaurant.domain.model.ReviewModel
import com.ucb.food.restaurant.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow

class GetReviewsUseCase(
    private val repository: RestaurantRepository
) {
    operator fun invoke(branchId: String): Flow<List<ReviewModel>> = repository.getReviews(branchId)
}
