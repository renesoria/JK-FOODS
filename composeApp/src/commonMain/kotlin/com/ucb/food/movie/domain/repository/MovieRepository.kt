package com.ucb.food.movie.domain.repository

import com.ucb.food.movie.domain.model.MovieModel

interface MovieRepository {
    suspend fun getMovies(): List<MovieModel>
}