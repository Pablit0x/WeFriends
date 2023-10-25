package com.ps.wefriends.data.workers

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class UploadWorkerFactory @Inject constructor(
    private val storageReference: StorageReference, private val dispatcher: CoroutineDispatcher
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context, workerClassName: String, workerParameters: WorkerParameters
    ): ListenableWorker = UploadWorker(
        context = appContext,
        workerParameters = workerParameters,
        storageReference = storageReference,
        dispatcher = dispatcher
    )
}