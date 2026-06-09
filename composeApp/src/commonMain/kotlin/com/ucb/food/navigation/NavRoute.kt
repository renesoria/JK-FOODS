package com.ucb.food.navigation

import kotlinx.serialization.Serializable


@Serializable
sealed class NavRoute {


    @Serializable
    object Profile: NavRoute()


    @Serializable
    object ProfileEdit: NavRoute()

    @Serializable
    object Github: NavRoute()

    @Serializable
    object Movies: NavRoute()

    @Serializable
    object Crypto: NavRoute()

    @Serializable
    object FakeStore: NavRoute()

    @Serializable
    object CountryStore: NavRoute()

    @Serializable
    object FirebaseTest: NavRoute()

    @Serializable
    object Login: NavRoute()

    @Serializable
    object SignUp: NavRoute()

    @Serializable
    object Home: NavRoute()

    @Serializable
    object Onboarding: NavRoute()
    
    @Serializable
    data class RestaurantDetail(val id: String) : NavRoute()
    
    @Serializable
    data class AddReview(val restaurantId: String) : NavRoute()
    
    @Serializable
    object MyReviews: NavRoute()

    @Serializable
    object Explore: NavRoute()

    @Serializable
    object DesignSystem: NavRoute()
}
