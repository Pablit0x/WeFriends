package com.ps.wefriends.domain.use_case

import com.ps.wefriends.domain.repository.SurveysRepository

class GetSurveys(
    private var repository: SurveysRepository
) {
    operator fun invoke() = repository.getSurveys()
}