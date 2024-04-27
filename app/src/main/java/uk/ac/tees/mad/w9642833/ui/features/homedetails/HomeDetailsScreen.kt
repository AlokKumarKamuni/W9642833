package uk.ac.tees.mad.w9642833.ui.features.homedetails

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import uk.ac.tees.mad.w9642833.R
import uk.ac.tees.mad.w9642833.data.HouseObject
import uk.ac.tees.mad.w9642833.data.Properties

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeDetailsScreen(navController: NavController, propertyId: String) {

    var property by remember {
        mutableStateOf<Properties?>(null)
    }

    LaunchedEffect(propertyId) {
        property = HouseObject.house?.properties?.first {
            it.id == propertyId
        }
    }

    val pagerState = rememberPagerState(pageCount = {
        property?.propertyImages?.images?.size ?: 0
    })

    BackHandler {
        navController.popBackStack()
    }


    Column(
        Modifier
            .verticalScroll(rememberScrollState())
    ) {

        Box(modifier = Modifier.padding(bottom = 8.dp)) {
            Image(
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                painter = rememberAsyncImagePainter(property?.propertyImages?.mainImageSrc),
                contentDescription = "Image"
            )
            Text(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
                text = property?.price?.displayPrices?.get(0)?.displayPrice ?: "",
                fontSize = 16.sp
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = property?.numberOfImages.toString(),
                    fontSize = 16.sp
                )
                Icon(
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
                Text(text = "${property?.bedrooms} BedRooms")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_meeting_room_24),
                    contentDescription = "BED"
                )
                Text(text = "${(property?.bedrooms ?: 0) + 1} Rooms")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_bathroom_24),
                    contentDescription = "BED"
                )
                Text(text = "${property?.bathrooms} Bathrooms")
            }
        }

        Row(
            modifier = Modifier.padding(start = 12.dp, end = 24.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                Icons.Default.LocationOn,
                contentDescription = "BED"
            )
            Text(text = property?.displayAddress ?: "")
        }

        Divider(
            modifier = Modifier.padding(vertical = 6.dp, horizontal = 16.dp),
            thickness = 1.dp
        )

        Row(
            modifier = Modifier.padding(top = 10.dp, start = 12.dp, end = 24.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = property?.summary ?: "",
                lineHeight = 16.sp
            )
        }



        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .padding(bottom = 12.dp),
            pageSize = PageSize.Fixed(250.dp)
        ) { page ->
            Card(
                modifier = Modifier.padding(4.dp),
                elevation = CardDefaults.elevatedCardElevation(8.dp)
            ) {
                Image(
                    modifier = Modifier
                        .height(200.dp)
                        .fillMaxWidth(),
                    contentScale = ContentScale.FillBounds,
                    painter = rememberAsyncImagePainter(
                        property?.propertyImages?.images?.get(page)?.srcUrl ?: ""
                    ),
                    contentDescription = "Image"
                )
            }

        }


    }
}