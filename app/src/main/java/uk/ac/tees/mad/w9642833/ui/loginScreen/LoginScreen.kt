package uk.ac.tees.mad.w9642833.ui.loginScreen

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.ac.tees.mad.w9642833.R
import uk.ac.tees.mad.w9642833.component.VerticalSpace

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    modifier: Modifier,
    loginScreenViewModel: LoginScreenViewModel = hiltViewModel(),
    navigateToRegisterScreen: () -> Unit
) {
    var showPassword by remember {
        mutableStateOf(false)
    }
    var emailFieldError by remember {
        mutableStateOf(false)
    }
    var emailFieldErrorCause by remember {
        mutableStateOf<String?>(null)
    }

    var passwordFieldError by remember {
        mutableStateOf(false)
    }
    var passwordFieldErrorCause by remember {
        mutableStateOf<String?>(null)
    }
    var context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    text = "ComfyStay",
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = 20.sp
                )
            })
        }
    ) { innerPadding ->
        Column(
            modifier = modifier
                .padding(innerPadding)
                .padding(horizontal = 10.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            /**
             * Image
             */
            Image(
                painter = painterResource(id = R.drawable.house_img),
                contentDescription = "House Image"
            )

            /**
             *  Login
             */
            Text(
                text = "Login to your Account!",
                style = MaterialTheme.typography.titleMedium,
                fontSize = 20.sp
            )
            VerticalSpace(space = 30)

            /**
             * 3 Filled TextField to take user personal information.
             */
            /**
             * Email-Id
             */
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Email:",
                style = MaterialTheme.typography.titleSmall,
                fontSize = 15.sp
            )
            VerticalSpace(space = 5)
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = loginScreenViewModel.emailId,
                onValueChange = {
                    loginScreenViewModel.updateStates(UserActionOnLoginScreen.OnEmailFieldClick(it))
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Email, contentDescription = "Email")
                },
                isError = emailFieldError,
                supportingText = {
                    emailFieldErrorCause?.let {
                        Text(text = it)
                    }
                }
            )

            VerticalSpace(space = 10)
            /**
             * New Password
             */
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Password:",
                style = MaterialTheme.typography.titleSmall,
                fontSize = 15.sp
            )
            VerticalSpace(space = 5)
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = loginScreenViewModel.password,
                onValueChange = {
                    loginScreenViewModel.updateStates(
                        UserActionOnLoginScreen.OnPasswordFieldClick(
                            it
                        )
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Outlined.Lock, contentDescription = "Password")
                },
                visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
                trailingIcon = {
                    val image =
                        if (showPassword) Icons.Outlined.Visibility else Icons.Outlined.VisibilityOff
                    IconButton(onClick = {
                        showPassword = !showPassword
                    }) {
                        Icon(imageVector = image, contentDescription = "Show Password")
                    }
                },
                isError = passwordFieldError,
                supportingText = {
                    passwordFieldErrorCause?.let {
                        Text(text = it)
                    }
                },
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                    keyboardType = KeyboardType.Password
                )
            )

            VerticalSpace(space = 30)
            /**
             * Login Button
             */
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {

                    /**
                     * First email validation
                     */
                    if (loginScreenViewModel.emailId.isNotEmpty()) {
                        if (Patterns.EMAIL_ADDRESS.matcher(loginScreenViewModel.emailId)
                                .matches()
                        ) {
                            emailFieldError = false
                            emailFieldErrorCause = null
                        } else {
                            emailFieldError = true
                            emailFieldErrorCause = "Please enter valid email-id."
                        }
                    } else {
                        emailFieldError = true
                        emailFieldErrorCause = "Please enter your email-id"
                    }

                    /**
                     * Password Validation
                     */
                    if (loginScreenViewModel.password.isNotEmpty()) {
                        if (loginScreenViewModel.password.length in 6..15) {
                            passwordFieldError = false
                            passwordFieldErrorCause = null
                        } else {
                            passwordFieldError = true
                            passwordFieldErrorCause = "Password Length should be in between 6 and 15."
                        }
                    } else {
                        passwordFieldError = true
                        passwordFieldErrorCause = "Please enter your password"
                    }

                    if (emailFieldError || passwordFieldError) {
                        Toast.makeText(context, "Validation Failed", Toast.LENGTH_SHORT).show()
                        return@Button
                    } else {
                        Toast.makeText(context, "Validation Succeeded", Toast.LENGTH_SHORT).show()
                        loginScreenViewModel.signIn(
                            inSuccessCase = {
                                Toast.makeText(context, "Login Succeeded", Toast.LENGTH_SHORT).show()
                            },
                            inFailureCase = {
                                Toast.makeText(context, it, Toast.LENGTH_LONG).show()
                            }
                        )
                    }
                },
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "SignIn",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
            }

            /**
             * If user have an account, then take him to login page
             * Login Page Button.
             */
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Don't have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                TextButton(
                    onClick = navigateToRegisterScreen,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Create",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}