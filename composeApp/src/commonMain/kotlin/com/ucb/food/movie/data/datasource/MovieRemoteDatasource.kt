package com.ucb.food.movie.data.datasource

import com.ucb.food.movie.data.dto.MovieDto

interface MovieRemoteDatasource {
    suspend fun getList(): List<MovieDto>
}