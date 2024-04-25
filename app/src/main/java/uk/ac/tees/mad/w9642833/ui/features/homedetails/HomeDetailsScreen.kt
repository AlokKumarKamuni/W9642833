package uk.ac.tees.mad.w9642833.ui.features.homedetails

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import uk.ac.tees.mad.w9642833.R

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeDetailsScreen(navController: NavController) {

    val pagerState = rememberPagerState(pageCount = {
        4
    })

    Column {

        Box(modifier = Modifier.padding(bottom = 8.dp)) {
            Image(
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                painter = painterResource(id = R.drawable.rentifylogo),
                contentDescription = "Image"
            )
            Text(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(8.dp),
                text = "£100 pcm",
                fontSize = 16.sp
            )
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "5",
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
                Text(text = "3 BedRooms")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_meeting_room_24),
                    contentDescription = "BED"
                )
                Text(text = "3 Rooms")
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_bathroom_24),
                    contentDescription = "BED"
                )
                Text(text = "3 Bathrooms")
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
            Text(text = "Pembroke Street, Finnieston, Glasgow, G3")
        }

        Divider(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 16.dp),
            thickness = 1.dp
        )

        Row(
            modifier = Modifier.padding(top = 4.dp, start = 12.dp, end = 24.dp, bottom = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Beautiful, recently refurbished and newly furnished 3 bed NON HMO flat (NO LOUNGE) within the West Ends' enviable location Finnieston, Glasgow, G3. Available end of March 2024",
                lineHeight = 16.sp
            )
        }



        HorizontalPager(
            modifier = Modifier.padding(bottom = 12.dp),
            state = pagerState, pageSize = PageSize.Fixed(250.dp)) { page ->
            Card(
                modifier = Modifier.padding(4.dp),
                elevation = CardDefaults.elevatedCardElevation(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.rentifylogo),
                    contentDescription = "Image"
                )
            }

        }
    }
}