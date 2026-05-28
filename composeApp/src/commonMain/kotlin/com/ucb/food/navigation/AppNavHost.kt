package com.ucb.food.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ucb.food.country.presentation.screen.CountryScreen
import com.ucb.food.crypto.presentation.screen.CryptoScreen
import com.ucb.food.fakestore.presentation.screen.StoreScreen
import com.ucb.food.github.presentation.screen.GithubScreen
import com.ucb.food.movie.presentation.screen.MovieScreen
import com.ucb.food.firebase.FirebaseTestScreen
import com.ucb.food.login.presentation.screen.LoginScreen
import com.ucb.food.signin.presentation.screen.SigninScreen
import com.ucb.food.home.presentation.screen.HomeScreen
import com.ucb.food.onboarding.presentation.screen.OnboardingScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavHost(
    viewModel: NavigationViewModel = koinViewModel()
) {
    val startDestination by viewModel.startDestination.collectAsState()
    val navController = rememberNavController()

    if (startDestination == null) return

    startDestination?.let { destination ->
        NavHost(navController = navController, startDestination = destination) {
            composable<NavRoute.Onboarding> {
                OnboardingScreen(
                    onNavigateToHome = {
                        navController.navigate(NavRoute.Home) {
                            popUpTo(NavRoute.Onboarding) { inclusive = true }
                        }
                    }
                )
            }
            
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
            composable<NavRoute.Login> {
                LoginScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToSignUp = { navController.navigate(NavRoute.SignUp) },
                    onLoginSuccess = { navController.navigate(NavRoute.Home) }
                )
            }
            composable<NavRoute.SignUp> {
                SigninScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToLogin = { navController.navigate(NavRoute.Login) }
                )
            }
            composable<NavRoute.Home> {
                HomeScreen()
            }
        }
    }
}
