package uk.ac.tees.mad.w9642833.navutil

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import uk.ac.tees.mad.w9642833.ui.features.bookmarks.BookmarkScreen
import uk.ac.tees.mad.w9642833.ui.features.home.HomeScreen
import uk.ac.tees.mad.w9642833.ui.features.homedetails.HomeDetailsScreen
import uk.ac.tees.mad.w9642833.ui.features.login.LoginScreen
import uk.ac.tees.mad.w9642833.ui.features.login.RegistrationScreen
import uk.ac.tees.mad.w9642833.ui.features.profile.ProfileScreen
import uk.ac.tees.mad.w9642833.ui.features.splash.SplashScreen

@Composable
fun Navigation(navController: NavHostController) {

    NavHost(navController, startDestination = Screen.SplashScreen.route) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.BookMarksScreen.route) {
            BookmarkScreen(navController)
        }
        composable(route = "${Screen.HomeDetailsScreen.route}/{propertyId}",
            arguments = listOf(
                navArgument("propertyId") {
                    type = NavType.StringType
                    defaultValue = ""
                },
                )
            ) { backStackEntry ->
            val propertyId =
                backStackEntry.arguments?.getString("propertyId") ?: ""

            HomeDetailsScreen(navController,propertyId)
        }
        composable(Screen.ProfileScreen.route) {
            ProfileScreen(navController)
        }
        composable(Screen.RegisterScreen.route) {
            RegistrationScreen(navController)
        }
        composable(Screen.LoginScreen.route) {
            LoginScreen(navController)
        }
        composable(Screen.SplashScreen.route) {
            SplashScreen(navController)
        }
    }
}