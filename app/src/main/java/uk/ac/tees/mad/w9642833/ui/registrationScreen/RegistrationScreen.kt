package uk.ac.tees.mad.w9642833.ui.registrationScreen

import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import uk.ac.tees.mad.w9642833.component.VerticalSpace

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationScreen(
    modifier: Modifier,
    registrationScreenViewModel: RegistrationScreenViewModel = hiltViewModel(),
    navigateToLoginScreen: () -> Unit
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
    var newPasswordFieldError by remember {
        mutableStateOf(false)
    }
    var newPasswordErrorCause by remember {
        mutableStateOf<String?>(null)
    }
    var confirmPasswordFieldError by remember {
        mutableStateOf(false)
    }
    var confirmPasswordErrorCause by remember {
        mutableStateOf<String?>(null)
    }
    var context = LocalContext.current


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "New Account",
                        style = MaterialTheme.typography.titleMedium,
                        fontSize = 20.sp
                    )
                }
            )
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
             *  Create New Account Text
             */
            Text(
                text = "Create New Account",
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
                value = registrationScreenViewModel.emailId,
                onValueChange = {
                                registrationScreenViewModel.updatesStates(UserActionOnSignUpScreen.OnEmailFieldClick(emailId = it))
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
                value = registrationScreenViewModel.newPassword,
                onValueChange = {
                                registrationScreenViewModel.updatesStates(UserActionOnSignUpScreen.OnNewPasswordFieldClick(it))
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
                isError = newPasswordFieldError,
                supportingText = {
                    newPasswordErrorCause?.let {
                        Text(text = it)
                    }
                }
            )

            VerticalSpace(space = 10)
            /**
             * Confirm New Password
             */
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "Confirm Password:",
                style = MaterialTheme.typography.titleSmall,
                fontSize = 15.sp
            )
            VerticalSpace(space = 5)
            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = registrationScreenViewModel.confirmPassword,
                onValueChange = {
                                registrationScreenViewModel.updatesStates(UserActionOnSignUpScreen.OnConfirmPasswordClick(it))
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
                isError = confirmPasswordFieldError,
                supportingText = {
                    confirmPasswordErrorCause?.let {
                        Text(text = it)
                    }
                }
            )

            VerticalSpace(space = 30)
            /**
             * SignUp Button
             */
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    /**
                     * Email-ID Validation
                     */
                    if (registrationScreenViewModel.emailId.isNotEmpty()) {
                        if (Patterns.EMAIL_ADDRESS.matcher(registrationScreenViewModel.emailId)
                                .matches()
                        ) {
                            emailFieldError = false
                            emailFieldErrorCause = null
                        } else {
                            emailFieldError = true
                            emailFieldErrorCause = "Enter valid email-id"
                        }
                    } else {
                        emailFieldErrorCause = "Please enter your email-id"
                        emailFieldError = true
                    }

                    /**
                     * New and Confirm Password Field Validation
                     */
                    if (registrationScreenViewModel.newPassword.isNotEmpty() && registrationScreenViewModel.confirmPassword.isNotEmpty()) {

                        if ((registrationScreenViewModel.newPassword.length in 6..15) && (registrationScreenViewModel.confirmPassword.length in 6..15)) {
                            if (registrationScreenViewModel.newPassword == registrationScreenViewModel.confirmPassword) {
                                newPasswordFieldError = false
                                newPasswordErrorCause = null

                                confirmPasswordFieldError = false
                                confirmPasswordErrorCause = null
                            } else {
                                // passwords are matching or not
                                newPasswordFieldError = true
                                newPasswordErrorCause = "Passwords are not matching"

                                confirmPasswordFieldError = true
                                confirmPasswordErrorCause = "Passwords are not matching"
                            }
                        } else {
                            // Passwords length are 6 or 15 char
                            newPasswordFieldError = registrationScreenViewModel.newPassword.length !in 6..15
                            newPasswordErrorCause =
                                if (registrationScreenViewModel.newPassword.length in 6..15) null else "Password length should be 6 to 15 characters"

                            confirmPasswordFieldError =
                                registrationScreenViewModel.confirmPassword.length !in 6..15
                            confirmPasswordErrorCause =
                                if (registrationScreenViewModel.confirmPassword.length in 6..15) null else "Password length should be 6 to 15 characters"

                        }
                    } else {
                        // Passwords are empty or not
                        newPasswordFieldError = registrationScreenViewModel.newPassword.isEmpty()
                        newPasswordErrorCause =
                            if (registrationScreenViewModel.newPassword.isEmpty()) "Please enter your new password" else null

                        confirmPasswordFieldError = registrationScreenViewModel.confirmPassword.isEmpty()
                        confirmPasswordErrorCause =
                            if (registrationScreenViewModel.confirmPassword.isEmpty()) "Please enter your confirm password" else null
                    }
                    if ( emailFieldError || confirmPasswordFieldError || newPasswordFieldError) {
                        Toast.makeText(context, "Validation Failed", Toast.LENGTH_SHORT).show()
                        return@Button
                    } else {
                        Toast.makeText(context, "Validation Succeeded", Toast.LENGTH_SHORT).show()
                        Toast.makeText(context, "Validation Succeeded", Toast.LENGTH_SHORT).show()
                        registrationScreenViewModel.createAccount(
                            inSuccessCase = {
                                Toast.makeText(context, "Account Created Successfully", Toast.LENGTH_SHORT).show()
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
                    text = "SignUp",
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
                    text = "Already have an account?",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold
                )
                TextButton(
                    onClick = navigateToLoginScreen,
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Login",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}