package com.ucb.food.restaurant.domain.usecase

import com.ucb.food.restaurant.domain.model.ReviewModel
import com.ucb.food.restaurant.domain.repository.RestaurantRepository
import kotlinx.coroutines.flow.Flow

class GetUserReviewsUseCase(
    private val repository: RestaurantRepository
) {
    operator fun invoke(userId: String): Flow<List<ReviewModel>> = repository.getUserReviews(userId)
}
