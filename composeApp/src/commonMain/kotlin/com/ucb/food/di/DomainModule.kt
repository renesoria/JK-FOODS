package com.ucb.food.di

import com.ucb.food.fakestore.domain.usecase.GetStoreProductsUseCase
import com.ucb.food.github.domain.usecase.GetAvatarUseCase
import com.ucb.food.movie.domain.usecase.GetMoviesUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::GetAvatarUseCase)
    singleOf(::GetMoviesUseCase)

    singleOf(::GetStoreProductsUseCase)
}
