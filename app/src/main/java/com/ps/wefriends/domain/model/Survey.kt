package com.ps.wefriends.domain.model

data class Survey(
    var id: String? = null,
    var title: String? = null,
    var imageUrl: String? = null,
    var surveyType: String? = null,
    var genderAudience: String? = null
)
