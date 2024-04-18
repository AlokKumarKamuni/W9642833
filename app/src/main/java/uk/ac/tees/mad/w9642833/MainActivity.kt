package uk.ac.tees.mad.w9642833

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint
import uk.ac.tees.mad.w9642833.navigation.AppNavigation
import uk.ac.tees.mad.w9642833.navigation.Destinations
import uk.ac.tees.mad.w9642833.navigation.StartingDestinationViewModel
import uk.ac.tees.mad.w9642833.ui.registrationScreen.RegistrationScreen
import uk.ac.tees.mad.w9642833.ui.theme.ComfyStayTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val startingDestinationViewModel: StartingDestinationViewModel by viewModels()
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAuth = Firebase.auth
        setContent {
            ComfyStayTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val startDestination by startingDestinationViewModel._startingDestination.collectAsState()

                    var donePlaying by remember {
                        mutableStateOf(false)
                    }
                    //InitialAnimation(modifier = Modifier.fillMaxSize(), onDonePlaying = { donePlaying = true})
                    AnimatedVisibility(visible = !donePlaying) {
                        InitialAnimation(modifier = Modifier.fillMaxSize()) {
                            donePlaying = true
                        }
                    }
                    if (donePlaying){
                        AppNavigation(startingDestination = startDestination)
                    }
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        firebaseAuth.addAuthStateListener { auth ->
            val currentUser = auth.currentUser
            if (currentUser != null){
                startingDestinationViewModel.setsStartingDestination(
                    startingDestinations = Destinations.HomeScreen
                )
            } else {
                startingDestinationViewModel.setsStartingDestination(
                    startingDestinations = Destinations.PreAuth
                )
            }
        }
    }
}

@Composable
fun InitialAnimation(
    modifier: Modifier,
    onDonePlaying: () -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.animation1))
    var isPlaying by remember {
        mutableStateOf(true)
    }
    val progress by animateLottieCompositionAsState(
        composition = composition,
        isPlaying = isPlaying
    )
    LaunchedEffect(key1 = progress) {
        if (progress == 0f) {
            Log.d("Test", "Playing")
            isPlaying = true
        }
        if (progress == 1f) {
            Log.d("Test", "Done playing")
            isPlaying = false
            onDonePlaying()
        }
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        LottieAnimation(
            composition = composition,
            progress = {
                progress
            }
        )
    }
}

@Composable
fun NextScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Second Screen")
    }
}


