package com.example.test_usecase.model

import com.example.domainObject.game.game.TimeLimit
import com.example.testDomainObject.game.fake
import com.example.usecaseinterface.model.TimeLimitsUseCaseModel

fun TimeLimitsUseCaseModel.Companion.fake(
    blackTimeLimit: TimeLimit = TimeLimit.fake(),
    whiteTimeLimit: TimeLimit = TimeLimit.fake(),
): TimeLimitsUseCaseModel {
    return TimeLimitsUseCaseModel(
      blackTimeLimit = blackTimeLimit,
      whiteTimeLimit = whiteTimeLimit,
    )
}
