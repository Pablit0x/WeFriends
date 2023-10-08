package com.ps.wefriends.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.dataStoreFile
import com.ps.wefriends.domain.model.UserInfo
import com.ps.wefriends.domain.serializers.UserInfoSerializer
import com.ps.wefriends.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    @Provides
    @Singleton
    fun provideUserInfoDataStore(@ApplicationContext context: Context): DataStore<UserInfo> {
        return DataStoreFactory.create(
            serializer = UserInfoSerializer,
            produceFile = { context.dataStoreFile(Constants.USER_INFO_FILE) }
        )
    }
}