package com.example.usecase.usecaseinterface.model

import com.example.domainObject.game.game.TimeLimit

data class TimeLimitsUseCaseModel(
    val blackTimeLimit: TimeLimit,
    val whiteTimeLimit: TimeLimit,
) {
    companion object
}
