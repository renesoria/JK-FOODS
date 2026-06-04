package com.ucb.food.restaurant.domain.usecase

import com.ucb.food.restaurant.domain.model.ReviewModel
import com.ucb.food.restaurant.domain.repository.RestaurantRepository

class AddReviewUseCase(
    private val repository: RestaurantRepository
) {
    suspend operator fun invoke(review: ReviewModel) = repository.addReview(review)
}
