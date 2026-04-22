package com.ucb.food.di

import com.ucb.food.counter.presentation.viewmodel.CounterViewModel
import com.ucb.food.country.presentation.viewmodel.CountryViewModel
import com.ucb.food.crypto.presentation.viewmodel.CryptoViewModel
import com.ucb.food.fakestore.presentation.viewmodel.FakeStoreViewModel
import com.ucb.food.github.presentation.viewmodel.GithubViewModel
import com.ucb.food.increment.presentation.viewmodel.IncrementViewModel
import com.ucb.food.movie.presentation.viewmodel.MovieViewModel
import com.ucb.food.nm.login.presentation.viewmodel.LoginViewModel
import com.ucb.food.product_detail.presentation.viewmodel.ProductDetailViewModel
import com.ucb.food.signin.presentation.viewmodel.SigninViewModel
import com.ucb.food.firebase.FirebaseTestViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val presentationModule = module {

    viewModelOf(
        ::ProductDetailViewModel)
    viewModelOf(::CounterViewModel)
    viewModelOf(::IncrementViewModel)
    viewModelOf(::LoginViewModel)
    viewModelOf(::GithubViewModel)
    viewModelOf(::SigninViewModel)
    viewModelOf(::MovieViewModel)
    viewModelOf(::CryptoViewModel)
    viewModelOf(::FakeStoreViewModel)
    viewModelOf(::CountryViewModel)
    viewModelOf(::FirebaseTestViewModel)
}