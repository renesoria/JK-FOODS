package com.ucb.food.movie.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class MovieResponseDto(
    val results: List<MovieDto>
)
