package uk.ac.tees.mad.w9642833.ui.features.profile

import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import uk.ac.tees.mad.w9642833.R
import uk.ac.tees.mad.w9642833.navutil.Screen
import uk.ac.tees.mad.w9642833.navutil.UtilFunctions
import java.io.File
import java.util.Objects
import java.util.UUID


@Composable
fun ProfileScreen(navController: NavHostController) {


    var isEditable by remember {
        mutableStateOf(false)
    }

    var showLoader by remember {
        mutableStateOf(false)
    }

    val context = LocalContext.current

    var userName by remember {
        mutableStateOf("")
    }

    var profileImageUri by remember {
        mutableStateOf<String?>(null)
    }

    var displayableAddress by remember {
        mutableStateOf("")
    }

    var shouldShowMap by remember {
        mutableStateOf(false)
    }

    var selectedLatLng by remember {
        mutableStateOf(LatLng(0.0, 0.0))
    }

    var user by remember {
        mutableStateOf<User?>(null)
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

    val firebaseStorage by remember {
        mutableStateOf(FirebaseStorage.getInstance())
    }

    LaunchedEffect(Unit) {

        currentUser?.uid?.let {
            firebaseFireStore.collection("users").document(it).get().addOnCompleteListener { task ->
                if (task.isSuccessful && task.result.exists()) {
                    user = task.result.toObject(User::class.java)
                }
            }
        }
    }


    LaunchedEffect(user) {
        userName = user?.name ?: ""
        selectedLatLng = LatLng(user?.latitude ?: 0.0, user?.longitude ?: 0.0)
        profileImageUri = user?.profileImageUrl

    }

    LaunchedEffect(selectedLatLng) {
        displayableAddress = UtilFunctions.getMarkerAddressDetails(
            selectedLatLng.latitude, selectedLatLng.longitude,
            context
        ) ?: ""
    }


    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "uk.ac.tees.mad.w9642833" + ".provider", file
    )


    val locationPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
            shouldShowMap = true
            Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show()
        }
    }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) {
            profileImageUri = uri?.toString()
        }

    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri1 ->
            profileImageUri = uri1?.toString()
        }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        if (it) {
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


        if (isEditable) {

            if (shouldShowMap) {
                MapScreen(
                    onMapClicked = { latLng ->
                        shouldShowMap = false
                        selectedLatLng = latLng
                    }
                )
            } else {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(vertical = 24.dp)
                ) {

                    Icon(
                        Icons.Filled.Close,
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(end = 24.dp)
                            .clickable {
                                isEditable = false
                            },
                        contentDescription = "Close"
                    )

                    Box(modifier = Modifier.align(Alignment.CenterHorizontally)) {
                        Image(
                            modifier = Modifier
                                .height(200.dp)
                                .width(200.dp)
                                .clip(CircleShape),
                            painter = if (profileImageUri != null)
                                rememberAsyncImagePainter(profileImageUri)
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
                                        cameraLauncher.launch(uri)
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
                        value = userName,
                        placeholder = {
                            Text(text = "Enter Username")
                        },
                        label = {
                            Text(text = "UserName")
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
                        trailingIcon = {
                            Icon(Icons.Filled.LocationOn, modifier = Modifier.clickable {

                                val permissionCheckResult =
                                    ContextCompat.checkSelfPermission(
                                        context,
                                        android.Manifest.permission.ACCESS_FINE_LOCATION
                                    )
                                if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
                                    shouldShowMap = true
                                } else {
                                    locationPermissionLauncher.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
                                }

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

                            showLoader = true
                            firebaseStorage.reference
                                .child(firebaseAuth.currentUser?.uid ?: "")
                                .putFile(Uri.parse(profileImageUri))
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

                        }) {
                        Text(text = "Save")
                    }

                }
            }

        } else {


            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 24.dp)
            ) {

                Button(
                    modifier = Modifier
                        .padding(12.dp)
                        .align(Alignment.End)
                        .height(40.dp),
                    onClick = {
                        isEditable = true
                    }) {
                    Text(text = "Edit")
                }

                Image(
                    modifier = Modifier
                        .height(200.dp)
                        .width(200.dp)
                        .align(Alignment.CenterHorizontally)
                        .clip(CircleShape),
                    painter = if (profileImageUri != null)
                        rememberAsyncImagePainter(profileImageUri)
                    else
                        painterResource(id = R.drawable.rentifylogo),
                    contentDescription = "Profile Image"
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .padding(horizontal = 24.dp)
                ) {
                    Icon(
                        Icons.Filled.AccountBox,
                        contentDescription = "Account"
                    )
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = userName,
                        fontSize = 24.sp
                    )
                }


                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(top = 24.dp)
                        .padding(horizontal = 24.dp)
                ) {
                    Icon(
                        Icons.Filled.LocationOn,
                        contentDescription = "Location"
                    )
                    Text(
                        modifier = Modifier.padding(start = 4.dp),
                        text = displayableAddress,
                        fontSize = 14.sp
                    )
                }

                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 20.dp, horizontal = 24.dp),
                    onClick = {
                        firebaseAuth.signOut()
                        navController.navigate(Screen.LoginScreen.route) {
                            popUpTo(Screen.ProfileScreen.route) {
                                inclusive = true
                            }
                        }
                    }) {
                    Text(text = "Logout")
                }
            }
        }
    }

}

@Composable
fun MapScreen(onMapClicked: (latLng: LatLng) -> Unit) {

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(0.0, 0.0), 15f)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GoogleMap(
            onMapClick = { latLng ->
                onMapClicked.invoke(latLng)
            },
            properties = MapProperties(isMyLocationEnabled = true),
            uiSettings = MapUiSettings(
                compassEnabled = true
            ),
            cameraPositionState = cameraPositionState,
        )
    }
}

fun Context.createImageFile(): File {
    val name = UUID.randomUUID().toString()
    val imageFileName = "JPEG_" + name + "_"
    val image = File.createTempFile(
        imageFileName,
        ".jpg",
        externalCacheDir
    )
    return image
}

