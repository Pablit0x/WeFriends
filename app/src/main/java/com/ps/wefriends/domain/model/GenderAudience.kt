package com.ps.wefriends.domain.model

enum class GenderAudience(val value: String) {
    MALE("MALE"), FEMALE("FEMALE"), ANY("ANY")
}
fun String.toGenderAudience() : GenderAudience{
    return when (this.uppercase()) {
        "MALE" -> GenderAudience.MALE
        "FEMALE" -> GenderAudience.FEMALE
        else -> GenderAudience.ANY
    }
}