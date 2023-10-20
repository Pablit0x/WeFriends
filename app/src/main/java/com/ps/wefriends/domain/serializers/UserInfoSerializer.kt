package com.ps.wefriends.domain.serializers

import androidx.datastore.core.Serializer
import com.ps.wefriends.domain.model.UserConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream

object UserInfoSerializer : Serializer<UserConfig> {
    override val defaultValue: UserConfig
        get() = UserConfig()

    override suspend fun readFrom(input: InputStream): UserConfig {
        return try {
            Json.decodeFromString(
                deserializer = UserConfig.serializer(),
                string = input.readBytes().decodeToString()
            )
        } catch (e: SerializationException) {
            e.printStackTrace()
            return defaultValue
        }
    }

    override suspend fun writeTo(t: UserConfig, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = UserConfig.serializer(), value = t
                ).encodeToByteArray()
            )
        }
    }
}