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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
import retrofit2.Call
import retrofit2.Response
import uk.ac.tees.mad.w9642833.R
import uk.ac.tees.mad.w9642833.data.ApiClient
import uk.ac.tees.mad.w9642833.data.HouseObject
import uk.ac.tees.mad.w9642833.data.Houses
import uk.ac.tees.mad.w9642833.data.Properties
import uk.ac.tees.mad.w9642833.navutil.Screen

@Composable
fun HomeScreen(navController: NavController) {

    val context = LocalContext.current

    var houses by remember {
        mutableStateOf<Houses?>(null)
    }

    var showLoader by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {

        showLoader = true
        val call = ApiClient.apiService.getHouseList(
            locationIdentifier = "REGION^61466",
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
            houses?.properties?.forEach { property ->
                HomeCard(navController = navController, property)

            }
        }
    }

}


@Composable
fun HomeCard(navController: NavController, property: Properties) {


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

            Row(
                modifier = Modifier.padding(start = 12.dp, end = 24.dp, bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.LocationOn,
                    contentDescription = "Address"
                )
                Text(text = property.displayAddress ?: "")
            }


        }
    }

}