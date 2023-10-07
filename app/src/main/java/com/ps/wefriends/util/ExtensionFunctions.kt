package com.ps.wefriends.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.ps.wefriends.domain.model.UserInfo
import com.ps.wefriends.domain.serializers.UserInfoSerializer

val Context.dataStore: DataStore<UserInfo> by dataStore(
    fileName = Constants.USER_INFO_FILE,
    serializer = UserInfoSerializer
)