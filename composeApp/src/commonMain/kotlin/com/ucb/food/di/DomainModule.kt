package com.ucb.food.di

import com.ucb.food.fakestore.domain.usecase.GetStoreProductsUseCase
import com.ucb.food.github.domain.usecase.GetAvatarUseCase
import com.ucb.food.login.domain.usecase.DoLoginUseCase
import com.ucb.food.login.domain.usecase.DoSignUpUseCase
import com.ucb.food.movie.domain.usecase.GetMoviesUseCase
import com.ucb.food.onboarding.domain.usecase.CompleteOnboardingUseCase
import com.ucb.food.onboarding.domain.usecase.GetOnboardingUseCase
import com.ucb.food.onboarding.domain.usecase.IsOnboardingCompletedUseCase
import com.ucb.food.profile.domain.usecase.GetUserProfileUseCase
import com.ucb.food.profile.domain.usecase.SyncProfileUseCase
import com.ucb.food.profile.domain.usecase.UpdateUserProfileUseCase
import com.ucb.food.restaurant.domain.usecase.AddReviewUseCase
import com.ucb.food.restaurant.domain.usecase.GetMenuUseCase
import com.ucb.food.restaurant.domain.usecase.GetRestaurantDetailsUseCase
import com.ucb.food.restaurant.domain.usecase.GetRestaurantsUseCase
import com.ucb.food.restaurant.domain.usecase.GetReviewsUseCase
import com.ucb.food.restaurant.domain.usecase.GetUserReviewsUseCase
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val domainModule = module {
    singleOf(::DoLoginUseCase)
    singleOf(::DoSignUpUseCase)
    singleOf(::GetAvatarUseCase)
    singleOf(::GetMoviesUseCase)

    singleOf(::GetStoreProductsUseCase)

    singleOf(::GetOnboardingUseCase)
    singleOf(::CompleteOnboardingUseCase)
    singleOf(::IsOnboardingCompletedUseCase)

    singleOf(::GetUserProfileUseCase)
    singleOf(::UpdateUserProfileUseCase)
    singleOf(::SyncProfileUseCase)

    // Restaurant
    singleOf(::GetRestaurantsUseCase)
    singleOf(::GetRestaurantDetailsUseCase)
    singleOf(::GetMenuUseCase)
    singleOf(::GetReviewsUseCase)
    singleOf(::GetUserReviewsUseCase)
    singleOf(::AddReviewUseCase)
}
