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
import com.ucb.food.login.data.repository.AuthRepositoryImpl
import com.ucb.food.login.data.service.FirebaseAuthService
import com.ucb.food.login.domain.repository.AuthenticationRepository
import com.ucb.food.onboarding.data.repository.OnboardingRepositoryImpl
import com.ucb.food.onboarding.domain.repository.OnboardingRepository
import com.ucb.food.restaurant.data.repository.RestaurantRepositoryImpl
import com.ucb.food.restaurant.domain.repository.RestaurantRepository
import com.russhwolf.settings.Settings
import com.russhwolf.settings.ObservableSettings
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataModule = module {
    single { 
        Json { 
            ignoreUnknownKeys = true 
            isLenient = true
        } 
    }
    single { 
        HttpClient {
            install(ContentNegotiation) {
                json(get())
            }
        }
    }
    single { Settings() }
    
    // Auth
    singleOf(::FirebaseAuthService)
    singleOf(::AuthRepositoryImpl).bind<AuthenticationRepository>()

    singleOf(::GitHubApiService).bind<GithubRemoteDataSource>()
    singleOf(::GithubRepositoryImpl).bind<GithubRepository>()
    
    singleOf(::MovieRepositoryImpl).bind<MovieRepository>()
    singleOf(::MovieService).bind<MovieRemoteDatasource>()

    // FakeStore
    singleOf(::ProductService).bind<ProductRemoteDatasource>()
    singleOf(::StoreRepositoryImpl).bind<StoreRepository>()

    singleOf(::OnboardingRepositoryImpl).bind<OnboardingRepository>()

    // Restaurant
    singleOf(::RestaurantRepositoryImpl).bind<RestaurantRepository>()

    single { FirebaseManager() }
}
