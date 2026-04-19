package com.ucb.food.movie.presentation.state

import com.ucb.food.movie.domain.model.MovieModel

data class MovieUiState(
    val isLoading: Boolean = false,
    val list: List<MovieModel> = emptyList()
)
