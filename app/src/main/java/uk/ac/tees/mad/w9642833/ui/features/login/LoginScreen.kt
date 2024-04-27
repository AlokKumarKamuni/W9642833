package uk.ac.tees.mad.w9642833.ui.features.login

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import uk.ac.tees.mad.w9642833.R
import uk.ac.tees.mad.w9642833.navutil.Screen

@Composable
fun LoginScreen(navController: NavController) {

    val context = LocalContext.current

    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    val firebaseAuth by remember {
        mutableStateOf(Firebase.auth)
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var showLoader by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit) {
        if (firebaseAuth.currentUser != null) {
            navController.navigate(Screen.HomeScreen.route) {
                popUpTo(Screen.LoginScreen.route) {
                    inclusive = true
                }
            }
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


        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                modifier = Modifier
                    .height(200.dp)
                    .width(200.dp)
                    .clip(CircleShape),
                painter = painterResource(id = R.drawable.rentifylogo),
                contentDescription = "Profile Image"
            )

            TextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                value = email,
                singleLine = true,
                placeholder = {
                    Text(text = "Enter Email")
                },
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
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
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
                    } else {
                        showLoader = true
                        firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener { signinTask ->
                                showLoader = false
                                if (signinTask.isSuccessful) {
                                    navController.navigate(Screen.HomeScreen.route) {
                                        popUpTo(Screen.LoginScreen.route) {
                                            inclusive = true
                                        }
                                    }
                                } else {
                                    Toast.makeText(context,"Failed to Login",Toast.LENGTH_SHORT).show()
                                }

                            }
                    }
                }) {
                Text(text = "Login")
            }

            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .clickable {
                        navController.navigate(Screen.RegisterScreen.route)
                    },
                text = "Registration",
                textAlign = TextAlign.Center
            )

        }
    }
}