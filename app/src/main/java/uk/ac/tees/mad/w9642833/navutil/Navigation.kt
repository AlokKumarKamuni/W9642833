package uk.ac.tees.mad.w9642833.navutil

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uk.ac.tees.mad.w9642833.ui.features.bookmarks.BookmarkScreen
import uk.ac.tees.mad.w9642833.ui.features.home.HomeScreen
import uk.ac.tees.mad.w9642833.ui.features.homedetails.HomeDetailsScreen

@Composable
fun Navigation(navController: NavHostController) {

    NavHost(navController, startDestination = Screen.HomeScreen.route) {
        composable(Screen.HomeScreen.route) {
            HomeScreen(navController = navController)
        }
        composable(Screen.BookMarksScreen.route) {
            BookmarkScreen(navController)
        }
        composable(Screen.HomeDetailsScreen.route) {
            HomeDetailsScreen(navController)
        }
        composable(Screen.ProfileScreen.route) {
            HomeDetailsScreen(navController)
        }
    }
}