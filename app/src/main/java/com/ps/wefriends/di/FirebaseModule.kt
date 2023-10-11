package com.ps.wefriends.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.ps.wefriends.data.repository.SurveysRepositoryImpl
import com.ps.wefriends.domain.repository.SurveysRepository
import com.ps.wefriends.domain.use_case.AddSurvey
import com.ps.wefriends.domain.use_case.GetSurveys
import com.ps.wefriends.domain.use_case.SurveyUseCases
import com.ps.wefriends.util.Constants.SURVEYS
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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
    fun provideBooksRepository(
        surveysRef: CollectionReference
    ): SurveysRepository = SurveysRepositoryImpl(surveysRef)

    @Provides
    fun provideUseCases(
        repo: SurveysRepository
    ) = SurveyUseCases(
        getSurveys = GetSurveys(repo),
        addSurvey = AddSurvey(repo)
    )

}