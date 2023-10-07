package com.ps.wefriends.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val showOnboarding: Boolean = true,
)