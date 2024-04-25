package uk.ac.tees.mad.w9642833.navutil

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uk.ac.tees.mad.w9642833.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    shouldShow: Boolean = true,
    shouldShowBackIcon: Boolean = false,
    title: String
) {
    if (shouldShow) {

        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = colorResource(id = R.color.yellow),
                titleContentColor = colorResource(id = R.color.white),
                actionIconContentColor = colorResource(id = R.color.white),
                navigationIconContentColor = colorResource(id = R.color.white)
            ),
            navigationIcon = {

                if (shouldShowBackIcon) {
                    Icon(
                        Icons.Filled.ArrowBack,
                        modifier = Modifier
                            .padding(start = 8.dp)
                            .clickable {
                                navController.popBackStack()
                            },
                        contentDescription = "Back Arrow"
                    )
                }

            },
            title = {
                Text(
                    text = title,
                    modifier = Modifier.padding(start = 8.dp)
                )
            },
        )
    }
}