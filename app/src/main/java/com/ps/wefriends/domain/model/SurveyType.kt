package com.ps.wefriends.domain.model

enum class SurveyType(val value: String) {
    FORM(value = "FORM"), WOULD_YOU_RATHER(value = "WOULD YOU RATHER")
}


fun String.toSurveyType(): SurveyType {
    return when (this.uppercase()) {
        "FORM" -> SurveyType.FORM
        else -> SurveyType.WOULD_YOU_RATHER
    }
}