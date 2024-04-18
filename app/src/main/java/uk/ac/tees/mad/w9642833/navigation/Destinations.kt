package uk.ac.tees.mad.w9642833.navigation

sealed class Destinations(
    val route: String
) {
    data object PreAuth: Destinations(route = "PreAuth"){
        data object LoginScreen: Destinations(route = "LoginScreen")
        data object RegistrationScreen: Destinations(route = "RegistrationScreen")
    }
    data object HomeScreen: Destinations(route = "HomeScreen")
}