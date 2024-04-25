package uk.ac.tees.mad.w9642833.navutil

object UtilFunctions {
    fun shouldShowAppBar(route:String) : Boolean {
       return when(route) {
            Screen.HomeScreen.route , Screen.HomeDetailsScreen.route, Screen.ProfileScreen.route, Screen.BookMarksScreen.route -> true
            else -> false
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
            else -> ""
        }
    }
 }