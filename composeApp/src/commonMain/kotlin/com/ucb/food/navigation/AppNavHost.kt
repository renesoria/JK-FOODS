package com.ucb.food.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ucb.food.country.presentation.screen.CountryScreen
import com.ucb.food.crypto.presentation.screen.CryptoScreen
import com.ucb.food.fakestore.presentation.screen.StoreScreen
import com.ucb.food.github.presentation.screen.GithubScreen
import com.ucb.food.movie.presentation.screen.MovieScreen
import com.ucb.food.firebase.FirebaseTestScreen

@Composable
fun AppNavHost() {


    val navController = rememberNavController()


    NavHost(navController = navController, startDestination = NavRoute.FirebaseTest) {
        composable<NavRoute.Profile> {

        }

        composable<NavRoute.ProfileEdit> {

        }
        composable<NavRoute.Github> {
            GithubScreen()
        }
        composable<NavRoute.Movies> {
            MovieScreen()
        }
        composable<NavRoute.Crypto> {
            CryptoScreen()
        }
        composable<NavRoute.FakeStore> {
            StoreScreen()
        }
        composable<NavRoute.CountryStore> {
            CountryScreen()
        }
        composable<NavRoute.FirebaseTest> {
            FirebaseTestScreen()
        }
    }
}
