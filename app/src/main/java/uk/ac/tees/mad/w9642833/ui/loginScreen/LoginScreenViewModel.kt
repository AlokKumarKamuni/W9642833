package uk.ac.tees.mad.w9642833.ui.loginScreen

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
class LoginScreenViewModel @Inject constructor(
    private val accountRepository: AccountRepository
): ViewModel() {

    var emailId by mutableStateOf("")
        private set
    var password by mutableStateOf("")
        private set

    fun updateStates(
        event: UserActionOnLoginScreen
    ){
        when(event){
            is UserActionOnLoginScreen.OnEmailFieldClick -> {
                this.emailId = event.emailId
            }
            is UserActionOnLoginScreen.OnPasswordFieldClick -> {
                this.password = event.password
            }
        }
    }

    fun signIn(
        inSuccessCase: () -> Unit,
        inFailureCase: (String) -> Unit
    ){
        viewModelScope.launch {
            accountRepository.login(
                email = emailId,
                password = password,
                onSuccess = {
                    inSuccessCase
                },
                onFailure = {
                    inFailureCase(it)
                }
            )
        }
    }
    fun resetStates(){
        emailId = ""
        password = ""
    }
}