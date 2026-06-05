package com.ucb.food.restaurant.presentation.state

sealed interface AddReviewEvent {
    data class LoadData(val restaurantId: String) : AddReviewEvent
    data class OnRatingChanged(val rating: Int) : AddReviewEvent
    data class OnCommentChanged(val comment: String) : AddReviewEvent
    data class OnPhotoSelected(val base64: String) : AddReviewEvent
    data class OnDishToggle(val dishId: String) : AddReviewEvent
    
    // Toggles for checklist
    data object OnToggleParking : AddReviewEvent
    data object OnToggleWifi : AddReviewEvent
    data object OnTogglePayment : AddReviewEvent
    data object OnTogglePetFriendly : AddReviewEvent
    data object OnToggleKidsArea : AddReviewEvent
    data object OnToggleFastService : AddReviewEvent
    data object OnToggleClean : AddReviewEvent
    data object OnToggleCozy : AddReviewEvent
    data object OnToggleFairPrice : AddReviewEvent
    
    data object OnSubmitClick : AddReviewEvent
    data object OnCancelClick : AddReviewEvent
}
