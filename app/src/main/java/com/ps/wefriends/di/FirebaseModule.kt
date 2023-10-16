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
import com.ps.wefriends.domain.use_case.AddSurvey
import com.ps.wefriends.domain.use_case.GetSurveys
import com.ps.wefriends.domain.use_case.SignInAsGuest
import com.ps.wefriends.presentation.screens.authentication.AuthClient
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
    fun provideBooksRepository(
        surveysRef: CollectionReference
    ): SurveysRepository = SurveysRepositoryImpl(surveysRef)

    @Provides
    @Singleton
    fun provideGetSurveysUseCase(
        repo: SurveysRepository
    ) = GetSurveys(repo)

    @Provides
    @Singleton
    fun provideAddSurveyUseCase(
        repo: SurveysRepository
    ) = AddSurvey(repo)


    @Provides
    @Singleton
    fun provideSignInAsGuest(
        firebaseAuth: FirebaseAuth
    ) = SignInAsGuest(firebaseAuth)

    @Provides
    @Singleton
    fun provideSignInClient(@ApplicationContext context: Context): SignInClient {
        return Identity.getSignInClient(context)
    }


    @Provides
    @Singleton
    fun provideAuthClient(
        signInClient: SignInClient, firebaseAuth: FirebaseAuth
    ): AuthClient {
        return AuthClient(signInClient = signInClient, firebaseAuth = firebaseAuth)
    }

}