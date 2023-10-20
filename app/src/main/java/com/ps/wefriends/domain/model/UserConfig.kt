package com.ps.wefriends.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class UserConfig(
    val showOnboarding: Boolean = true
)