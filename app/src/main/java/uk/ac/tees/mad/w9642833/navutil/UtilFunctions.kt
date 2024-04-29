package uk.ac.tees.mad.w9642833.navutil

import android.content.Context
import android.location.Geocoder
import com.google.android.gms.maps.model.LatLng
import java.util.Locale
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

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

    fun distanceBetweenTwoLatLng(latlng1: LatLng, latlng2: LatLng): Double {
        val lat1 = latlng1.latitude
        val lon1 = latlng1.longitude
        val lat2 = latlng2.latitude
        val lon2 = latlng2.longitude

        val R = 6371 // Radius of the earth in km
        val dLat = deg2rad(lat2 - lat1)
        val dLon = deg2rad(lon2 - lon1)
        val a =
            sin(dLat / 2) * sin(dLat / 2) + cos(deg2rad(lat1)) * cos(deg2rad(lat2)) * sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        val distance = R * c

        return distance
    }

    private fun deg2rad(deg: Double): Double {
        return deg * (PI / 180)
    }
 }