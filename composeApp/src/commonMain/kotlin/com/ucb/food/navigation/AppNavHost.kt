package com.ucb.food.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.coroutines.launch
import com.ucb.food.country.presentation.screen.CountryScreen
import com.ucb.food.crypto.presentation.screen.CryptoScreen
import com.ucb.food.fakestore.presentation.screen.StoreScreen
import com.ucb.food.github.presentation.screen.GithubScreen
import com.ucb.food.movie.presentation.screen.MovieScreen
import com.ucb.food.firebase.FirebaseTestScreen
import com.ucb.food.login.presentation.screen.LoginScreen
import com.ucb.food.profile.presentation.screen.ProfileEditScreen
import com.ucb.food.profile.presentation.screen.ProfileScreen
import com.ucb.food.signin.presentation.screen.SigninScreen
import com.ucb.food.home.presentation.screen.HomeScreen
import com.ucb.food.onboarding.presentation.screen.OnboardingScreen
import com.ucb.food.restaurant.presentation.screen.RestaurantDetailScreen
import com.ucb.food.restaurant.presentation.screen.AddReviewScreen
import com.ucb.food.restaurant.presentation.screen.ExploreRestaurantsScreen
import com.ucb.food.profile.presentation.screen.MyReviewsScreen
import com.ucb.food.core.presentation.screen.DesignSystemScreen
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun AppNavHost(
    viewModel: NavigationViewModel = koinViewModel()
) {
    val startDestination by viewModel.startDestination.collectAsState()
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    // Obtenemos la ruta actual para la barra inferior
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    if (startDestination == null) return

    startDestination?.let { destination ->
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                NavDrawerContent(
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(NavRoute.Home) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                    onCloseDrawer = { scope.launch { drawerState.close() } }
                )
            }
        ) {
            Scaffold(
                bottomBar = {
                    // Lógica manual para convertir el destination a NavRoute
                    // (En una app pro se usa una extensión, aquí lo simplificamos)
                    val currentRoute = when {
                        currentDestination?.route?.contains("Home") == true -> NavRoute.Home
                        currentDestination?.route?.contains("Profile") == true -> NavRoute.Profile
                        currentDestination?.route?.contains("MyReviews") == true -> NavRoute.MyReviews
                        currentDestination?.route?.contains("Explore") == true -> NavRoute.Explore
                        else -> null
                    }
                    
                    BottomNavigationBar(
                        currentRoute = currentRoute,
                        onNavigate = { route ->
                            navController.navigate(route) {
                                popUpTo(NavRoute.Home) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            ) { padding ->
                Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                    NavHost(navController = navController, startDestination = destination) {
                        composable<NavRoute.Onboarding> {
                            OnboardingScreen(
                                onNavigateToLogin = {
                                    navController.navigate(NavRoute.Login) {
                                        popUpTo(NavRoute.Onboarding) { inclusive = true }
                                    }
                                }
                            )
                        }
                        
                        composable<NavRoute.Profile> {
                            ProfileScreen(
                                onNavigateToEditProfile = { navController.navigate(NavRoute.ProfileEdit) },
                                onNavigateToMyReviews = { navController.navigate(NavRoute.MyReviews) },
                                onNavigateToLogin = {
                                    navController.navigate(NavRoute.Login) {
                                        popUpTo(NavRoute.Home) { inclusive = true }
                                    }
                                },
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable<NavRoute.ProfileEdit> {
                            ProfileEditScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
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
                                onNavigateToLogin = { navController.navigate(NavRoute.Login) },
                                onNavigateToHome = {
                                    navController.navigate(NavRoute.Home) {
                                        popUpTo(NavRoute.Login) { inclusive = true }
                                    }
                                }
                            )
                        }
                        composable<NavRoute.Home> {
                            HomeScreen(
                                onNavigateToProfile = { navController.navigate(NavRoute.Profile) },
                                onNavigateToLogin = {
                                    navController.navigate(NavRoute.Login) {
                                        popUpTo(NavRoute.Home) { inclusive = true }
                                    }
                                },
                                onNavigateToRestaurantDetail = { id ->
                                    navController.navigate(NavRoute.RestaurantDetail(id))
                                },
                                onNavigateToExplore = {
                                    navController.navigate(NavRoute.Explore)
                                },
                                onNavigateToDesignSystem = {
                                    navController.navigate(NavRoute.DesignSystem)
                                },
                                onOpenDrawer = {
                                    scope.launch { drawerState.open() }
                                }
                            )
                        }
                        
                        composable<NavRoute.RestaurantDetail> { backStackEntry ->
                            val route: NavRoute.RestaurantDetail = backStackEntry.toRoute()
                            RestaurantDetailScreen(
                                restaurantId = route.id,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToAddReview = { id ->
                                    navController.navigate(NavRoute.AddReview(id))
                                }
                            )
                        }
                        
                        composable<NavRoute.AddReview> { backStackEntry ->
                            val route: NavRoute.AddReview = backStackEntry.toRoute()
                            AddReviewScreen(
                                restaurantId = route.restaurantId,
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                        
                        composable<NavRoute.MyReviews> {
                            MyReviewsScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }

                        composable<NavRoute.Explore> {
                            ExploreRestaurantsScreen(
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToDetail = { id ->
                                    navController.navigate(NavRoute.RestaurantDetail(id))
                                }
                            )
                        }

                        composable<NavRoute.DesignSystem> {
                            DesignSystemScreen(
                                onNavigateBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
