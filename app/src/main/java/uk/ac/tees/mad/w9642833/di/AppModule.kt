package uk.ac.tees.mad.w9642833.di

import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uk.ac.tees.mad.w9642833.repository.accountRepository.AccountRepository
import uk.ac.tees.mad.w9642833.repository.accountRepository.AccountRepositoryImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesAccountRepoInstance(
        firebaseAuth: FirebaseAuth
    ): AccountRepository {
        return AccountRepositoryImpl(firebaseAuth = firebaseAuth)
    }
}