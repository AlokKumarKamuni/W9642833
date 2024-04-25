package uk.ac.tees.mad.w9642833.navutil

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.tees.mad.w9642833.R

@Composable
fun BottomNav(currentRoute: String, navController: NavController, shouldShow: Boolean) {

    if (shouldShow) {


        BottomNavigation(
            backgroundColor = colorResource(id = R.color.yellow),
        ) {


            BottomNavigationItem(
                alwaysShowLabel = false,
                modifier = Modifier.padding(top = 8.dp),
                selected = currentRoute == Screen.BookMarksScreen.route,
                onClick = {

                    if (currentRoute != Screen.BookMarksScreen.route) {
                        navController.navigate(Screen.BookMarksScreen.route) {
                            popUpTo(Screen.HomeScreen.route)
                        }
                    }
                },
                icon = {
                    Icon(
                        Icons.Filled.Star,
                        tint = Color.White,
                        contentDescription = "BookMarks"
                    )
                },
                label = {
                    Text(
                        "BookMarks", color = Color.White,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            )

            BottomNavigationItem(
                alwaysShowLabel = false,
                modifier = Modifier.padding(top = 8.dp),
                selected = currentRoute == Screen.HomeScreen.route,
                onClick = {
                    if (currentRoute != Screen.HomeScreen.route) {
                        navController.popBackStack()
                    }
                },
                icon = {
                    Icon(
                        Icons.Filled.Home,
                        tint = Color.White,
                        contentDescription = "Home"
                    )
                },
                label = {
                    Text(
                        "Home", color = Color.White,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            )
            BottomNavigationItem(
                alwaysShowLabel = false,
                modifier = Modifier.padding(top = 8.dp),
                selected = currentRoute == Screen.ProfileScreen.route,
                onClick = {
                    if (currentRoute != Screen.ProfileScreen.route) {
                        navController.navigate(Screen.ProfileScreen.route) {
                            popUpTo(Screen.HomeScreen.route)
                        }
                    }
                },
                icon = {
                    Icon(
                        Icons.Filled.AccountBox,
                        tint = Color.White,
                        contentDescription = "Profile"
                    )
                },
                label = {
                    Text(
                        "Profile", color = Color.White,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )
                }
            )
        }
    }
}