package com.ucb.food.movie.data.mapper

import com.ucb.food.movie.data.dto.MovieDto
import com.ucb.food.movie.domain.model.MovieModel

fun MovieDto.toModel() = MovieModel(
    description = "",
    pathUrl = "https://image.tmdb.org/t/p/w185$posterPath",
    title = title
)
