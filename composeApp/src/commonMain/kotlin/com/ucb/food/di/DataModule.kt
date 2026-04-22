package com.ucb.food.di

import com.ucb.food.fakestore.data.datasource.ProductRemoteDatasource
import com.ucb.food.fakestore.data.repository.StoreRepositoryImpl
import com.ucb.food.fakestore.data.service.ProductService
import com.ucb.food.fakestore.domain.repository.StoreRepository
import com.ucb.food.github.data.datasource.GithubRemoteDataSource
import com.ucb.food.github.data.repository.GithubRepositoryImpl
import com.ucb.food.github.data.service.GitHubApiService
import com.ucb.food.github.domain.repository.GithubRepository
import com.ucb.food.movie.data.datasource.MovieRemoteDatasource
import com.ucb.food.movie.data.repository.MovieRepositoryImpl
import com.ucb.food.movie.data.service.MovieService
import com.ucb.food.movie.domain.repository.MovieRepository
import com.ucb.food.portafolio.data.datasource.FirebaseManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    singleOf(::GitHubApiService).bind<GithubRemoteDataSource>()
    singleOf(::GithubRepositoryImpl).bind<GithubRepository>()
    
    singleOf(::MovieRepositoryImpl).bind<MovieRepository>()
    singleOf(::MovieService).bind<MovieRemoteDatasource>()

    // FakeStore
    singleOf(::ProductService).bind<ProductRemoteDatasource>()
    singleOf(::StoreRepositoryImpl).bind<StoreRepository>()

    single { FirebaseManager() }
}
