package uk.ac.tees.mad.w9642833.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import uk.ac.tees.mad.w9642833.ui.homeScreen.HomeScreen
import uk.ac.tees.mad.w9642833.ui.loginScreen.LoginScreen
import uk.ac.tees.mad.w9642833.ui.registrationScreen.RegistrationScreen

@Composable
fun AppNavigation(
    navHostController: NavHostController = rememberNavController(),
    startingDestination: Destinations
) {
    NavHost(navController = navHostController, startDestination = startingDestination.route){
        /**
         * With below navigation() function we define nested navigation.
         */
        navigation(
            startDestination = Destinations.PreAuth.LoginScreen.route,
            route = Destinations.PreAuth.route
        ){
            composable(
                route = Destinations.PreAuth.LoginScreen.route
            ){
                LoginScreen(
                    modifier = Modifier.fillMaxSize(),
                    navigateToRegisterScreen = {
                        navigateAndPop(
                            navHostController = navHostController,
                            route = Destinations.PreAuth.RegistrationScreen.route,
                            popUpDestination = Destinations.PreAuth.LoginScreen.route
                        )
                    }
                )
            }
            
            composable(
                route = Destinations.PreAuth.RegistrationScreen.route
            ){
                RegistrationScreen(modifier = Modifier.fillMaxSize(),navigateToLoginScreen = {
                    navigateAndPop(
                        navHostController = navHostController,
                        route = Destinations.PreAuth.LoginScreen.route,
                        popUpDestination = Destinations.PreAuth.RegistrationScreen.route
                    )
                })
            }
        }

        composable(
            route = Destinations.HomeScreen.route
        ){
            HomeScreen(modifier = Modifier.fillMaxWidth())
        }
    }
}

fun navigateAndPop(
    navHostController: NavHostController,
    route: String,
    popUpDestination: String
){
    navHostController.navigate(route){
        launchSingleTop = true
        popUpTo(popUpDestination){
            inclusive = true
        }
    }
}