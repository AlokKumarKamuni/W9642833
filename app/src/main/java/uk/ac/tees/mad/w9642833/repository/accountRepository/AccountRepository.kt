package uk.ac.tees.mad.w9642833.repository.accountRepository

import com.google.firebase.auth.FirebaseUser

interface AccountRepository {

    /**
     * createUserAccount() - This function we can create new user account on firebase server with its credentials i.e., email and password.
     */
    suspend fun createUserAccount(
        userEmailId: String,
        newPassword: String,
        onSuccess: (FirebaseUser?) -> Unit,
        onFailure: (String) -> Unit
    )

    /**
     * Through this function user can logout from the app.
     */
    suspend fun logOut()

    /**
     * signIn() - Through this function user can login with their credentials.
     */
    fun login(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    )
}