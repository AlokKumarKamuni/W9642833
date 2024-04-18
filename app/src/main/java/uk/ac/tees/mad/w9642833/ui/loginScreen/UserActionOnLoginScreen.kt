package uk.ac.tees.mad.w9642833.ui.loginScreen

import uk.ac.tees.mad.w9642833.ui.registrationScreen.UserActionOnSignUpScreen

sealed interface UserActionOnLoginScreen {
    data class OnEmailFieldClick(val emailId: String): UserActionOnLoginScreen
    data class OnPasswordFieldClick(val password: String): UserActionOnLoginScreen
}