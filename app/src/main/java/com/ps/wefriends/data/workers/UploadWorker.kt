package com.ps.wefriends.data.workers

import android.content.Context
import android.net.Uri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.storage.StorageReference
import com.ps.wefriends.util.Constants.KEY_IMAGE_URI
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.io.IOException

@HiltWorker
class UploadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    @Assisted private val storageReference: StorageReference,
    @Assisted private val dispatcher: CoroutineDispatcher
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result = withContext(dispatcher) {
        val inputFileUri = inputData.getString(KEY_IMAGE_URI)
        return@withContext try {
            uploadImageFromUri(Uri.parse(inputFileUri))
        } catch (exception: Exception) {
            exception.printStackTrace()
            Result.failure()
        } catch (e: IOException) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun uploadImageFromUri(fileUri: Uri): Result {
        fileUri.lastPathSegment?.let {
            val photoRef = storageReference.child(it)
            Timber.d("Upload Worker", it)
            photoRef.putFile(fileUri)
        }
        return Result.success()
    }
}