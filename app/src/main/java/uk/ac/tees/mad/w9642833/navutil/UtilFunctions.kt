package uk.ac.tees.mad.w9642833.navutil

import android.content.Context
import android.location.Geocoder
import java.util.Locale

object UtilFunctions {
    fun shouldShowAppBar(route:String) : Boolean {
       return when(route) {
            Screen.LoginScreen.route , Screen.RegisterScreen.route, Screen.SplashScreen.route -> false
            else -> true
        }
    }

    fun shouldShowBottomBar(route:String) : Boolean {
        return when(route) {
            Screen.HomeScreen.route , Screen.ProfileScreen.route, Screen.BookMarksScreen.route -> true
            else -> false
        }
    }

    fun getAppBarTitle(route:String) : String {
        return when(route) {
            Screen.HomeScreen.route -> "Rentify"
            Screen.HomeDetailsScreen.route -> "Home Details"
            Screen.ProfileScreen.route -> "Profile"
            Screen.BookMarksScreen.route -> "Bookmarks"
            else -> "Home Details"
        }
    }


    fun getMarkerAddressDetails(lat: Double, long: Double, context: Context): String? {


        val geocoder = Geocoder(context, Locale.getDefault())

        val addresses = geocoder.getFromLocation(lat, long, 1)

        return if (!addresses.isNullOrEmpty()) {
            addresses[0].featureName + ", " + addresses[0].locality + ", " + addresses[0].adminArea
        } else {
            null
        }
    }
 }