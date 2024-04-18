package uk.ac.tees.mad.w9642833.ui.registrationScreen

sealed interface UserActionOnSignUpScreen {
    data class OnEmailFieldClick(val emailId: String): UserActionOnSignUpScreen
    data class OnNewPasswordFieldClick(val newPassword: String): UserActionOnSignUpScreen
    data class OnConfirmPasswordClick(val confirmPassword: String): UserActionOnSignUpScreen
}