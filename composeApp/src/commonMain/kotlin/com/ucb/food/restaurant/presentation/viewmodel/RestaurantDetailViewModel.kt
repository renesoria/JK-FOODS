package com.ucb.food.restaurant.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.food.restaurant.domain.usecase.GetMenuUseCase
import com.ucb.food.restaurant.domain.usecase.GetRestaurantDetailsUseCase
import com.ucb.food.restaurant.domain.usecase.GetReviewsUseCase
import com.ucb.food.restaurant.presentation.state.RestaurantDetailEffect
import com.ucb.food.restaurant.presentation.state.RestaurantDetailEvent
import com.ucb.food.restaurant.presentation.state.RestaurantDetailState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class RestaurantDetailViewModel(
    private val getRestaurantDetailsUseCase: GetRestaurantDetailsUseCase,
    private val getMenuUseCase: GetMenuUseCase,
    private val getReviewsUseCase: GetReviewsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(RestaurantDetailState())
    val state = _state.asStateFlow()

    private val _effect = Channel<RestaurantDetailEffect>()
    val effect = _effect.receiveAsFlow()

    fun onEvent(event: RestaurantDetailEvent) {
        when (event) {
            is RestaurantDetailEvent.LoadDetails -> loadAll(event.id)
            RestaurantDetailEvent.OnBackClick -> viewModelScope.launch { _effect.send(RestaurantDetailEffect.NavigateBack) }
            RestaurantDetailEvent.OnAddReviewClick -> {
                state.value.restaurant?.id?.let { id ->
                    viewModelScope.launch { _effect.send(RestaurantDetailEffect.NavigateToAddReview(id)) }
                }
            }
        }
    }

    private fun loadAll(id: String) {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            // Obtenemos los detalles primero para tener el branchId
            getRestaurantDetailsUseCase(id).flatMapLatest { restaurant ->
                val branchId = restaurant?.branches?.firstOrNull()?.id ?: ""
                
                combine(
                    flowOf(restaurant),
                    getMenuUseCase(id),
                    getReviewsUseCase(branchId)
                ) { rest, menu, reviews ->
                    Triple(rest, menu, reviews)
                }
            }.collect { (restaurant, menu, reviews) ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        restaurant = restaurant,
                        menu = menu,
                        reviews = reviews
                    )
                }
            }
        }
    }
}
