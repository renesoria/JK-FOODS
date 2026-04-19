package com.ucb.food.movie.data.repository

import com.ucb.food.movie.data.datasource.MovieRemoteDatasource
import com.ucb.food.movie.data.mapper.toModel
import com.ucb.food.movie.domain.model.MovieModel
import com.ucb.food.movie.domain.repository.MovieRepository

class MovieRepositoryImpl(
    val remote: MovieRemoteDatasource
): MovieRepository {
    override suspend fun getMovies(): List<MovieModel> {
        val response = remote.getList()
        return response.map { it.toModel() }
    }
}