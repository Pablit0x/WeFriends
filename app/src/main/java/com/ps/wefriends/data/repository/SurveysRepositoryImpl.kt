package com.ps.wefriends.data.repository


import com.google.firebase.firestore.CollectionReference
import com.ps.wefriends.domain.model.Survey
import com.ps.wefriends.domain.repository.SurveysRepository
import com.ps.wefriends.util.Response
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class SurveysRepositoryImpl(
    private val surveysCollectionRef: CollectionReference
) : SurveysRepository {

    override fun getSurveys() = callbackFlow {
        val snapshotListener = surveysCollectionRef.addSnapshotListener { snapshot, e ->
            val surveysResponse = if (snapshot != null) {
                val surveys = snapshot.toObjects(Survey::class.java)
                Response.Success(surveys)
            } else {
                Response.Failure(e)
            }
            trySend(surveysResponse)
        }
        awaitClose {
            snapshotListener.remove()
        }
    }

    override suspend fun addSurvey(
        title: String, imageUrl: String?, surveyType: String, genderAudience: String
    ) = try {
        val id = surveysCollectionRef.document().id
        val survey = Survey(
            id = id,
            title = title,
            imageUrl = imageUrl,
            surveyType = surveyType,
            genderAudience = genderAudience
        )
        surveysCollectionRef.document(id).set(survey).await()
        Response.Success(true)
    } catch (e: Exception) {
        Response.Failure(e)
    }
}