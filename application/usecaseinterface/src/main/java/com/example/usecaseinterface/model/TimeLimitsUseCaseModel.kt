package com.example.usecaseinterface.model

import com.example.domainObject.game.game.TimeLimit

data class TimeLimitsUseCaseModel(
    val blackTimeLimit: TimeLimit,
    val whiteTimeLimit: TimeLimit,
    val timeOver: TimeOverUseCaseModel,
) {
    companion object
}
