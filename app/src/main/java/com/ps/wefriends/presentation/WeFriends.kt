package com.ps.wefriends.presentation

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.ps.wefriends.data.workers.UploadWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class WeFriends : Application(), Configuration.Provider {

    @Inject
    lateinit var uploadWorkerFactory: UploadWorkerFactory
    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(uploadWorkerFactory)
            .build()


}