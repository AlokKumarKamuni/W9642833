package uk.ac.tees.mad.w9642833.ui.features.bookmarks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import uk.ac.tees.mad.w9642833.data.HouseObject
import uk.ac.tees.mad.w9642833.data.Preferences
import uk.ac.tees.mad.w9642833.data.Properties
import uk.ac.tees.mad.w9642833.ui.features.home.HomeCard

@Composable
fun BookmarkScreen(navController: NavController) {
    val context = LocalContext.current

    var houses by remember {
        mutableStateOf<List<Properties>?>(emptyList())
    }



    LaunchedEffect(Unit) {

        val allHouses = HouseObject.house

        val prefSet = Preferences.getPref(context)?.getStringSet("bookmarks", emptySet())

        houses = allHouses?.properties?.filter {
            prefSet?.contains(it.id) == true
        }
    }



    Column(modifier = Modifier.verticalScroll(state = rememberScrollState())) {
        houses?.forEach { property ->
            HomeCard(navController = navController, property)
        }
    }
}


