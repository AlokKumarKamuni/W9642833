package uk.ac.tees.mad.w9642833.ui.features.login

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import uk.ac.tees.mad.w9642833.BuildConfig
import uk.ac.tees.mad.w9642833.R
import uk.ac.tees.mad.w9642833.navutil.Screen
import uk.ac.tees.mad.w9642833.navutil.UtilFunctions
import uk.ac.tees.mad.w9642833.ui.features.profile.MapScreen
import uk.ac.tees.mad.w9642833.ui.features.profile.User
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects
import java.util.UUID

@Composable
fun RegistrationScreen(navController: NavController) {


    val context = LocalContext.current

    var userName by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    var capturedImageUri by remember {
        mutableStateOf<Uri>(Uri.EMPTY)
    }

    var displayableAddress by remember {
        mutableStateOf("")
    }

    var shouldShowMap by remember {
        mutableStateOf(false)
    }

    val firebaseStorage by remember {
        mutableStateOf(FirebaseStorage.getInstance())
    }

    var selectedLatLng by remember {
        mutableStateOf(LatLng(0.0, 0.0))
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var file by remember {
        mutableStateOf<File?>(null)
    }

    var uri by remember {
        mutableStateOf<Uri?>(null)
    }



    val firebaseAuth by remember {
        mutableStateOf(Firebase.auth)
    }

    val firebaseFireStore by remember {
        mutableStateOf(Firebase.firestore)
    }

    var fusedLocationClient by remember {
        mutableStateOf<FusedLocationProviderClient?>(null)
    }


    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient?.lastLocation?.addOnSuccessListener { location: Location? ->
                selectedLatLng = LatLng(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
            }
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }



    LaunchedEffect(Unit) {



        file = context.createImageFile()
        uri = FileProvider.getUriForFile(
            Objects.requireNonNull(context),
            BuildConfig.APPLICATION_ID + ".provider", file!!
        )

        val permissionCheckResult =
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
            fusedLocationClient?.lastLocation?.addOnSuccessListener { location: Location? ->
                selectedLatLng = LatLng(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
            }
        } else {
            locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }

    }

    LaunchedEffect(selectedLatLng) {
        displayableAddress = UtilFunctions.getMarkerAddressDetails(
            selectedLatLng.latitude, selectedLatLng.longitude,
            context
        ) ?: ""
    }




    var showLoader by remember {
        mutableStateOf(false)
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            capturedImageUri = uri!!
        }

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri1 ->
            capturedImageUri = uri1?:Uri.EMPTY
        }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            cameraLauncher.launch(uri!!)
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
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

        if (shouldShowMap) {
            MapScreen(
                selectedLatLng,
                onMapClicked = { latLng ->
                    shouldShowMap = false
                    selectedLatLng = latLng
                }
            )
        } else {

            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 24.dp)
            ) {

                Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                    Image(
                        modifier = Modifier
                            .height(200.dp)
                            .width(200.dp)
                            .clip(CircleShape),
                        painter = if (capturedImageUri.path?.isNotEmpty() == true)
                            rememberAsyncImagePainter(capturedImageUri)
                        else
                            painterResource(id = R.drawable.rentifylogo),
                        contentDescription = "Profile Image"
                    )

                    Icon(
                        modifier = Modifier
                            .clickable {
                                val permissionCheckResult =
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        android.Manifest.permission.CAMERA
                                    )
                                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                    cameraLauncher.launch(uri!!)
                                } else {
                                    cameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
                                }
                            }
                            .align(Alignment.BottomStart)
                            .padding(start = 20.dp, bottom = 20.dp),
                        painter = painterResource(id = R.drawable.baseline_camera_alt_24),
                        contentDescription = "Camera"
                    )

                    Icon(
                        modifier = Modifier
                            .clickable {
                                galleryLauncher.launch(
                                    PickVisualMediaRequest(
                                        mediaType = ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            }
                            .align(Alignment.BottomEnd)
                            .padding(end = 20.dp, bottom = 20.dp),
                        painter = painterResource(id = R.drawable.baseline_add_a_photo_24),
                        contentDescription = "Gallery"
                    )
                }



                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = email,
                    placeholder = {
                        Text(text = "Enter Email")
                    },
                    singleLine = true,

                    label = {
                        Text(text = "Email")
                    },
                    onValueChange = {
                        email = it
                    }
                )



                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = password,
                    singleLine = true,
                    trailingIcon = {
                        val image = if (passwordVisible)
                            painterResource(id = R.drawable.baseline_visibility_24)
                        else
                            painterResource(id = R.drawable.baseline_visibility_off_24)


                        val description = if (passwordVisible) "Hide password" else "Show password"

                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(image, description)
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    placeholder = {
                        Text(text = "Enter Password")
                    },
                    label = {
                        Text(text = "Password")
                    },
                    onValueChange = {
                        password = it
                    }
                )


                TextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    value = userName,
                    placeholder = {
                        Text(text = "Enter Username")
                    },
                    singleLine = true,
                    label = {
                        Text(text = "Name")
                    },
                    onValueChange = {
                        userName = it
                    }
                )


                TextField(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    readOnly = true,
                    value = displayableAddress,
                    onValueChange = { _ -> },
                    singleLine = true,

                    trailingIcon = {
                        Icon(Icons.Filled.LocationOn, modifier = Modifier.clickable {

                            shouldShowMap = true

                        }, contentDescription = "Choose")
                    },
                    placeholder = {
                        Text(text = "Select Location")
                    })

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    onClick = {

                        if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            Toast.makeText(context, "Enter Valid Email", Toast.LENGTH_SHORT).show()
                        } else if (password.isEmpty() || password.length < 6) {
                            Toast.makeText(
                                context,
                                "Password should of length more than 6",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (userName.isEmpty()) {
                            Toast.makeText(context, "Enter Valid Username", Toast.LENGTH_SHORT)
                                .show()
                        } else if (capturedImageUri == Uri.EMPTY) {
                            Toast.makeText(
                                context,
                                "Please Choose Valid profile Photo",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else if (selectedLatLng.latitude == 0.0 || selectedLatLng.longitude == 0.0) {
                            Toast.makeText(context, "Choose Valid address", Toast.LENGTH_SHORT)
                                .show()
                        } else {
                            showLoader = true
                            firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener { loginTask ->
                                    if (loginTask.isSuccessful) {

                                        firebaseStorage.reference
                                            .child(firebaseAuth.currentUser?.uid ?: "")
                                            .putFile(capturedImageUri)
                                            .addOnCompleteListener { imageTask ->
                                                if (imageTask.isSuccessful) {
                                                    firebaseStorage.reference.child(
                                                        firebaseAuth.currentUser?.uid ?: ""
                                                    )
                                                        .downloadUrl.addOnCompleteListener { downTask ->
                                                            if (downTask.isSuccessful) {

                                                                val use = User(
                                                                    userName,
                                                                    downTask.result.toString(),
                                                                    selectedLatLng.latitude,
                                                                    selectedLatLng.longitude
                                                                )
                                                                firebaseFireStore.collection("users")
                                                                    .document(
                                                                        firebaseAuth.currentUser?.uid
                                                                            ?: ""
                                                                    )
                                                                    .set(use)
                                                                    .addOnCompleteListener { finalTask ->
                                                                        showLoader = false
                                                                        if (finalTask.isSuccessful) {
                                                                            navController.navigate(
                                                                                Screen.HomeScreen.route
                                                                            ) {
                                                                                popUpTo(Screen.LoginScreen.route) {
                                                                                    inclusive = true
                                                                                }
                                                                            }
                                                                            Toast.makeText(
                                                                                context,
                                                                                "Success",
                                                                                Toast.LENGTH_SHORT
                                                                            ).show()
                                                                        } else {
                                                                            firebaseAuth.signOut()
                                                                            Toast.makeText(
                                                                                context,
                                                                                "Failed, Try again  here ${finalTask.exception?.message}",
                                                                                Toast.LENGTH_SHORT
                                                                            ).show()
                                                                        }

                                                                    }
                                                            }
                                                        }
                                                } else {
                                                    showLoader = false
                                                    firebaseAuth.signOut()
                                                    Toast.makeText(
                                                        context,
                                                        "Failed, Try again",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }

                                            }

                                    } else {
                                        showLoader = false
                                        Toast.makeText(
                                            context,
                                            "Failed, Try again",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                }
                        }
                    }) {
                    Text(text = "Register")
                }

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .clickable {
                            navController.popBackStack()
                        },
                    text = "Login",
                    textAlign = TextAlign.Center
                )

            }
        }
    }
}

fun Context.createImageFile(): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image
}