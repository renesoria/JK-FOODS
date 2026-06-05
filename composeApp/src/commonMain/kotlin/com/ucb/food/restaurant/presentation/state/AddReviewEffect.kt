package com.ucb.food.restaurant.presentation.state

sealed interface AddReviewEffect {
    data object NavigateBack : AddReviewEffect
    data class ShowError(val message: String) : AddReviewEffect
}
