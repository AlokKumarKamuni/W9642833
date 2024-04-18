package uk.ac.tees.mad.w9642833.ui.homeScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.ac.tees.mad.w9642833.repository.accountRepository.AccountRepository
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val accountRepository: AccountRepository
): ViewModel() {
    fun signOut(){
        viewModelScope.launch {
            accountRepository.logOut()
        }
    }
}