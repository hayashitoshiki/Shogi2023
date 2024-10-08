package com.example.testusecase.model

import com.example.domainObject.game.game.TimeLimit
import com.example.testDomainObject.game.fake
import com.example.usecaseinterface.model.TimeLimitsUseCaseModel
import com.example.usecaseinterface.model.TimeOverUseCaseModel

fun TimeLimitsUseCaseModel.Companion.fake(
    blackTimeLimit: TimeLimit = TimeLimit.fake(),
    whiteTimeLimit: TimeLimit = TimeLimit.fake(),
    timeOver: TimeOverUseCaseModel = TimeOverUseCaseModel.None,
): TimeLimitsUseCaseModel {
    return TimeLimitsUseCaseModel(
        blackTimeLimit = blackTimeLimit,
        whiteTimeLimit = whiteTimeLimit,
        timeOver = timeOver,
    )
}
