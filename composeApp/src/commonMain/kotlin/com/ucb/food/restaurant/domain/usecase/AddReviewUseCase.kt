package com.ucb.food.restaurant.domain.usecase

import com.ucb.food.restaurant.domain.model.ReviewModel
import com.ucb.food.restaurant.domain.repository.RestaurantRepository

class AddReviewUseCase(
    private val repository: RestaurantRepository
) {
    suspend operator fun invoke(review: ReviewModel) {
        // 1. Guardar la reseña en la base de datos
        repository.addReview(review)

        // 2. Si la calificación es de 5 estrellas, enviar la notificación
        if (review.rating == 5) {
            val dishName = review.consumedDishes.firstOrNull() ?: "un excelente plato"
            repository.sendFiveStarNotification(
                restaurantName = review.restaurantName,
                dishName = dishName
            )
        }
    }
}
