package com.ucb.food.movie.domain.usecase

import com.ucb.food.movie.domain.model.MovieModel
import com.ucb.food.movie.domain.repository.MovieRepository

class GetMoviesUseCase(
    val repository: MovieRepository
) {
    suspend fun invoke(): List<MovieModel> {
        return repository.getMovies()
    }
}