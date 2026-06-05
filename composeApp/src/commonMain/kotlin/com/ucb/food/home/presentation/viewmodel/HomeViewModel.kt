package com.ucb.food.home.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ucb.food.home.presentation.state.HomeEffect
import com.ucb.food.home.presentation.state.HomeEvent
import com.ucb.food.home.presentation.state.HomeState
import com.ucb.food.restaurant.domain.usecase.GetHotDealsUseCase
import com.ucb.food.restaurant.domain.usecase.GetRestaurantsUseCase
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.auth.auth
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class HomeViewModel(
    private val getRestaurantsUseCase: GetRestaurantsUseCase,
    private val getHotDealsUseCase: GetHotDealsUseCase
) : ViewModel() {
    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    private val _effect = Channel<HomeEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        loadData()
    }

    private fun loadData() {
        _state.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            combine(
                getRestaurantsUseCase(),
                getHotDealsUseCase()
            ) { list, deals ->
                _state.update { 
                    it.copy(
                        isLoading = false,
                        restaurants = list,
                        filteredRestaurants = filterList(list, it.searchQuery), // Solo para el buscador
                        topRatedRestaurants = list.sortedByDescending { r -> r.overallRating }.take(5),
                        hotDeals = deals
                    ) 
                }
            }.collect()
        }
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            HomeEvent.OnMenuClick -> viewModelScope.launch { _effect.send(HomeEffect.OpenMenu) }
            HomeEvent.OnProfileClick -> viewModelScope.launch { _effect.send(HomeEffect.NavigateToProfile) }
            HomeEvent.OnCartClick -> viewModelScope.launch { _effect.send(HomeEffect.NavigateToCart) }
            HomeEvent.OnLogoutClick -> logout()
            is HomeEvent.OnSearchQueryChanged -> {
                _state.update { 
                    it.copy(
                        searchQuery = event.query,
                        filteredRestaurants = filterList(it.restaurants, event.query)
                    )
                }
            }
            is HomeEvent.OnRestaurantClick -> {
                viewModelScope.launch { _effect.send(HomeEffect.NavigateToRestaurantDetail(event.id)) }
            }
        }
    }

    private fun filterList(list: List<com.ucb.food.restaurant.domain.model.RestaurantModel>, query: String): List<com.ucb.food.restaurant.domain.model.RestaurantModel> {
        if (query.isBlank()) return list
        return list.filter { it.name.contains(query, ignoreCase = true) }
    }

    private fun logout() {
        viewModelScope.launch {
            try {
                Firebase.auth.signOut()
                _effect.send(HomeEffect.NavigateToLogin)
            } catch (e: Exception) {
                // handle error
            }
        }
    }
}
