package uk.ac.tees.mad.w9642833.ui.features.home

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Slider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import retrofit2.Call
import retrofit2.Response
import uk.ac.tees.mad.w9642833.R
import uk.ac.tees.mad.w9642833.data.ApiClient
import uk.ac.tees.mad.w9642833.data.HouseObject
import uk.ac.tees.mad.w9642833.data.Houses
import uk.ac.tees.mad.w9642833.data.Preferences
import uk.ac.tees.mad.w9642833.data.Properties
import uk.ac.tees.mad.w9642833.navutil.Screen
import uk.ac.tees.mad.w9642833.navutil.UtilFunctions
import uk.ac.tees.mad.w9642833.ui.features.profile.User
import kotlin.math.roundToInt

@Composable
fun HomeScreen(navController: NavController) {

    val context = LocalContext.current

    var houses by remember {
        mutableStateOf<Houses?>(null)
    }

    var showLoader by remember {
        mutableStateOf(false)
    }


    val firebaseAuth by remember {
        mutableStateOf(Firebase.auth)
    }
    val currentUser by remember {
        mutableStateOf(firebaseAuth.currentUser)
    }

    val firebaseFireStore by remember {
        mutableStateOf(Firebase.firestore)
    }

    var user by remember {
        mutableStateOf<User?>(null)
    }

    var userLocation by remember {
        mutableStateOf<LatLng>(LatLng(0.0, 0.0))
    }

    var sliderPosition by remember { mutableFloatStateOf(50f) }


    LaunchedEffect(Unit) {

        showLoader = true
        val call = ApiClient.apiService.getHouseList(
            locationIdentifier = "REGION^87490",
            channel = "RENT",
            currencyCode = "GBP"
        )
        call.enqueue(object : retrofit2.Callback<Houses> {
            override fun onResponse(
                call: Call<Houses>,
                response: Response<Houses>
            ) {
                if (response.isSuccessful) {
                    houses = response.body()
                    HouseObject.house = houses


                    currentUser?.uid?.let {
                        firebaseFireStore.collection("users").document(it).get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful && task.result.exists()) {
                                    user = task.result.toObject(User::class.java)
                                    userLocation =
                                        LatLng(user?.latitude ?: 0.0, user?.longitude ?: 0.0)
                                }
                            }
                    }




                    showLoader = false
                } else {
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Houses>, t: Throwable) {
                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    if (showLoader) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .width(40.dp)
                    .height(40.dp)
            )
        }

    } else {


        Column(modifier = Modifier.verticalScroll(state = rememberScrollState())) {


            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                Text(text = "Search Property in ")

                Slider(
                    value = sliderPosition,
                    onValueChange = { sliderPosition = it.roundToInt().toFloat() },
                    steps = 300,
                    valueRange = 0f..300f
                )
                Text(text = sliderPosition.toString() + "KM")
            }




            houses?.properties?.forEach { property ->

                if (UtilFunctions.distanceBetweenTwoLatLng(
                        userLocation,
                        LatLng(
                            property.location?.latitude ?: 0.0,
                            property.location?.longitude ?: 0.0
                        )
                    ) <= sliderPosition
                ) {
                    HomeCard(navController = navController, property)
                }


            }
        }
    }

}


@Composable
fun HomeCard(navController: NavController, property: Properties) {

    val context = LocalContext.current


    var isBookmarked by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        isBookmarked = Preferences.getPref(context)?.getStringSet("bookmarks", emptySet())
            ?.contains(property.id) == true
    }

    Card(
        modifier = Modifier
            .padding(6.dp)
            .clickable {
                navController.navigate(Screen.HomeDetailsScreen.withArgs(property.id ?: ""))
            },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.elevatedCardElevation(12.dp)
    ) {

        Column() {

            Box(modifier = Modifier.padding(bottom = 8.dp)) {
                Image(
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    painter = rememberAsyncImagePainter(model = property.propertyImages?.mainImageSrc),
                    contentDescription = "Image"
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp),
                    color = Color.White,
                    text = property.price?.displayPrices?.get(0)?.displayPrice ?: "",
                    fontSize = 16.sp
                )
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        color = Color.White,

                        text = property.numberOfImages.toString(),
                        fontSize = 16.sp
                    )
                    Icon(
                        tint = Color.White,
                        modifier = Modifier.padding(start = 4.dp),
                        painter = painterResource(id = R.drawable.baseline_image_24),
                        contentDescription = "Image"
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_single_bed_24),
                        contentDescription = "BED"
                    )
                    Text(text = "${property.bedrooms} BedRooms")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_meeting_room_24),
                        contentDescription = "Rooms"
                    )
                    Text(text = "${property.bedrooms + 1} Rooms")
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_bathroom_24),
                        contentDescription = "Bath"
                    )
                    Text(text = "${property.bathrooms} Bathrooms")
                }
            }

            Row {
                Row(
                    modifier = Modifier
                        .padding(start = 12.dp, end = 36.dp, bottom = 12.dp)
                        .weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.LocationOn,
                        contentDescription = "Address"
                    )
                    Text(text = property.displayAddress ?: "")
                }

                Icon(
                    modifier = Modifier.clickable {
                        val existingSet =
                            Preferences.getPref(context)?.getStringSet("bookmarks", emptySet())
                                ?.toMutableSet() ?: mutableSetOf()
                        val prefEditor = Preferences.getPref(context)?.edit()
                        if (isBookmarked) {
                            existingSet.remove(property.id)

                        } else {
                            existingSet.add(property.id)
                        }
                        prefEditor?.putStringSet("bookmarks", existingSet)?.commit()
                        isBookmarked = !isBookmarked

                    },
                    tint = if (isBookmarked) Color.Yellow else Color.White,
                    painter = painterResource(id = R.drawable.baseline_bookmark_add_24),
                    contentDescription = "bookmark"
                )
            }


        }
    }

}