package com.ps.wefriends.domain.model
data class UserData(
    val userId: String,
    val isAnonymous: Boolean,
    val username: String?,
    val profilePictureUrl: String?
)
