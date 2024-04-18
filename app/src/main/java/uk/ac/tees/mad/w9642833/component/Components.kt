package uk.ac.tees.mad.w9642833.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun VerticalSpace(
    space: Int
) {
    Spacer(modifier = Modifier.height(space.dp))
}