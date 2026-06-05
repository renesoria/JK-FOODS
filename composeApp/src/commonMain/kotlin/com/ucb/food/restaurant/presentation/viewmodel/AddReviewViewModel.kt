package com.ucb.food.restaurant.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.food.login.data.repository.UserDao
import com.ucb.food.restaurant.domain.model.ReviewModel
import com.ucb.food.restaurant.domain.usecase.AddReviewUseCase
import com.ucb.food.restaurant.domain.usecase.GetMenuUseCase
import com.ucb.food.restaurant.domain.usecase.GetRestaurantDetailsUseCase
import com.ucb.food.restaurant.presentation.state.AddReviewEffect
import com.ucb.food.restaurant.presentation.state.AddReviewEvent
import com.ucb.food.restaurant.presentation.state.AddReviewState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AddReviewViewModel(
    private val getRestaurantDetailsUseCase: GetRestaurantDetailsUseCase,
    private val getMenuUseCase: GetMenuUseCase,
    private val addReviewUseCase: AddReviewUseCase,
    private val userDao: UserDao
) : ViewModel() {

    private val _state = MutableStateFlow(AddReviewState())
    val state = _state.asStateFlow()

    private val _effect = Channel<AddReviewEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: AddReviewEvent) {
        when (event) {
            is AddReviewEvent.LoadData -> loadData(event.restaurantId)
            is AddReviewEvent.OnRatingChanged -> _state.update { it.copy(rating = event.rating) }
            is AddReviewEvent.OnCommentChanged -> _state.update { it.copy(comment = event.comment) }
            is AddReviewEvent.OnPhotoSelected -> _state.update { it.copy(photoBase64 = event.base64) }
            is AddReviewEvent.OnDishToggle -> toggleDish(event.dishId)
            
            AddReviewEvent.OnToggleParking -> _state.update { it.copy(hasParking = !it.hasParking) }
            AddReviewEvent.OnToggleWifi -> _state.update { it.copy(hasWifi = !it.hasWifi) }
            AddReviewEvent.OnTogglePayment -> _state.update { it.copy(acceptsDigitalPayment = !it.acceptsDigitalPayment) }
            AddReviewEvent.OnTogglePetFriendly -> _state.update { it.copy(isPetFriendly = !it.isPetFriendly) }
            AddReviewEvent.OnToggleKidsArea -> _state.update { it.copy(hasKidsArea = !it.hasKidsArea) }
            AddReviewEvent.OnToggleFastService -> _state.update { it.copy(isFastService = !it.isFastService) }
            AddReviewEvent.OnToggleClean -> _state.update { it.copy(isClean = !it.isClean) }
            AddReviewEvent.OnToggleCozy -> _state.update { it.copy(isCozy = !it.isCozy) }
            AddReviewEvent.OnToggleFairPrice -> _state.update { it.copy(isFairPrice = !it.isFairPrice) }
            
            AddReviewEvent.OnSubmitClick -> submitReview()
            AddReviewEvent.OnCancelClick -> viewModelScope.launch { _effect.send(AddReviewEffect.NavigateBack) }
        }
    }

    private fun loadData(restaurantId: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            combine(
                getRestaurantDetailsUseCase(restaurantId),
                getMenuUseCase(restaurantId)
            ) { restaurant, menu ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        restaurant = restaurant,
                        menu = menu
                    )
                }
            }.collect()
        }
    }

    private fun toggleDish(dishId: String) {
        _state.update { 
            val current = it.selectedDishes.toMutableSet()
            if (current.contains(dishId)) current.remove(dishId) else current.add(dishId)
            it.copy(selectedDishes = current)
        }
    }

    private fun submitReview() {
        val s = _state.value
        if (s.rating == 0) {
            viewModelScope.launch { _effect.send(AddReviewEffect.ShowError("Por favor, selecciona una calificación")) }
            return
        }

        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            try {
                val user = userDao.getCurrentUserFlow().firstOrNull()
                
                // IMPORTANTE: Aseguramos que el branchId no esté vacío
                val branchId = s.restaurant?.branches?.firstOrNull()?.id ?: ""
                
                if (branchId.isBlank()) {
                    _state.update { it.copy(isLoading = false) }
                    _effect.send(AddReviewEffect.ShowError("Error: No se encontró la sucursal"))
                    return@launch
                }

                val review = ReviewModel(
                    userId = user?.userId ?: "",
                    userName = "${user?.firstName} ${user?.lastName}",
                    userProfilePicture = user?.profilePicture,
                    restaurantId = s.restaurant?.id ?: "",
                    restaurantName = s.restaurant?.name ?: "", // Enviamos el nombre
                    branchId = branchId,
                    rating = s.rating,
                    comment = s.comment,
                    photoUrl = s.photoBase64,
                    consumedDishes = s.selectedDishes.toList(),
                    hasParking = s.hasParking,
                    hasWifi = s.hasWifi,
                    acceptsDigitalPayment = s.acceptsDigitalPayment,
                    isPetFriendly = s.isPetFriendly,
                    hasKidsArea = s.hasKidsArea,
                    isFastService = s.isFastService,
                    isClean = s.isClean,
                    isCozy = s.isCozy,
                    isFairPrice = s.isFairPrice
                )
                
                addReviewUseCase(review)
                _effect.send(AddReviewEffect.NavigateBack)
            } catch (e: Exception) {
                _state.update { it.copy(isLoading = false, error = e.message) }
                _effect.send(AddReviewEffect.ShowError(e.message ?: "Error al enviar review"))
            }
        }
    }
}
