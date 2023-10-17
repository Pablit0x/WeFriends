package com.ps.wefriends.di

import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ps.wefriends.data.repository.SurveysRepositoryImpl
import com.ps.wefriends.domain.repository.SurveysRepository
import com.ps.wefriends.presentation.screens.authentication.AuthUiClient
import com.ps.wefriends.util.Constants.SURVEYS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    @Singleton
    fun provideSurveysRef() = Firebase.firestore.collection(SURVEYS)


    @Provides
    @Singleton
    fun provideSurveysRepository(
        surveysRef: CollectionReference
    ): SurveysRepository = SurveysRepositoryImpl(surveysRef)


    @Provides
    @Singleton
    fun provideSignInClient(@ApplicationContext context: Context): SignInClient {
        return Identity.getSignInClient(context)
    }


    @Provides
    @Singleton
    fun provideAuthClient(
        signInClient: SignInClient, firebaseAuth: FirebaseAuth
    ): AuthUiClient {
        return AuthUiClient(signInClient = signInClient, firebaseAuth = firebaseAuth)
    }

}