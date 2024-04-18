package uk.ac.tees.mad.w9642833.ui.registrationScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.ac.tees.mad.w9642833.repository.accountRepository.AccountRepository
import javax.inject.Inject

@HiltViewModel
class RegistrationScreenViewModel @Inject constructor(
    private val accountRepository: AccountRepository
): ViewModel() {

    var emailId by mutableStateOf("")
        private set
    var newPassword by mutableStateOf("")
        private set
    var confirmPassword by mutableStateOf("")
        private set

    fun updatesStates(
        userAction: UserActionOnSignUpScreen
    ){
        when(userAction){
            is UserActionOnSignUpScreen.OnConfirmPasswordClick -> {
                this.confirmPassword = userAction.confirmPassword
            }
            is UserActionOnSignUpScreen.OnEmailFieldClick -> {
                this.emailId = userAction.emailId
            }
            is UserActionOnSignUpScreen.OnNewPasswordFieldClick -> {
                this.newPassword = userAction.newPassword
            }
        }
    }

    /**
     * createAccount() - Create account.
     */
    fun createAccount(
        inSuccessCase: () -> Unit,
        inFailureCase: (String) -> Unit
    ){
        viewModelScope.launch {
            accountRepository.createUserAccount(
                userEmailId = emailId,
                newPassword = newPassword,
                onSuccess = {
                    inSuccessCase()
                }
                ,
                onFailure = {
                    inFailureCase(it)
                }
            )
        }
    }

    fun resetStates(){
        emailId = ""
        newPassword = ""
        confirmPassword = ""
    }
}