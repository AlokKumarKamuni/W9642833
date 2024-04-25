package uk.ac.tees.mad.w9642833

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.w9642833.navutil.BottomNav
import uk.ac.tees.mad.w9642833.navutil.Navigation
import uk.ac.tees.mad.w9642833.navutil.TopBar
import uk.ac.tees.mad.w9642833.navutil.UtilFunctions
import uk.ac.tees.mad.w9642833.ui.theme.ComfyStayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComfyStayTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
                Scaffold(

                    topBar = {
                        TopBar(
                            navController = navController,
                            title = UtilFunctions.getAppBarTitle(currentRoute ?: ""),
                            shouldShow = UtilFunctions.shouldShowAppBar(currentRoute ?: "")
                        )
                    },
                    bottomBar = {
                        BottomNav(
                            currentRoute = currentRoute?:"",
                            navController = navController,
                            UtilFunctions.shouldShowBottomBar(currentRoute?:"")
                        )
                    }

                ) { innerPadding ->

                    Box(modifier = Modifier.padding(innerPadding)) {
                        Navigation(navController)
                    }

                }

            }
        }
    }
}
