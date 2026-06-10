package com.ucb.food.map.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.food.map.presentation.state.MapEffect
import com.ucb.food.map.presentation.state.MapEvent
import com.ucb.food.map.presentation.state.MapState
import com.ucb.food.restaurant.domain.usecase.GetRestaurantsUseCase
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MapViewModel(
    private val getRestaurantsUseCase: GetRestaurantsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(MapState())
    val state = _state.asStateFlow()

    private val _effect = Channel<MapEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        onEvent(MapEvent.OnLoadRequested)
    }

    fun onEvent(event: MapEvent) {
        when (event) {
            MapEvent.OnLoadRequested -> loadRestaurants()
            MapEvent.OnBackClicked -> viewModelScope.launch { _effect.send(MapEffect.NavigateBack) }
        }
    }

    private fun loadRestaurants() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getRestaurantsUseCase()
                .catch { e ->
                    _state.update { it.copy(isLoading = false, error = e.message) }
                }
                .collect { list ->
                    _state.update { it.copy(isLoading = false, restaurants = list) }
                }
        }
    }
}
