package com.example.testusecase.usecase

import com.example.domainObject.game.rule.GameRule
import com.example.domainObject.game.rule.GameTimeLimitRule
import com.example.testDomainObject.rule.fake
import com.example.usecaseinterface.usecase.GameSettingUseCase

class FakeGameSettingUseCase : GameSettingUseCase {

    private var calSetGameRuleCount = 0
    var calGetTimeLimitsCount = 0
        private set

    fun getCalSetGameRuleCount() : Int = calSetGameRuleCount

    var setGameRuleLogic: (GameRule) -> Unit = { }
    var getTimeLimitsLogic: () -> GameTimeLimitRule = { GameTimeLimitRule.fake() }

    override suspend fun setGameRule(rule: GameRule) {
        calSetGameRuleCount += 1
        setGameRuleLogic(rule)
    }

    override suspend fun getTimeLimits(): GameTimeLimitRule {
        calGetTimeLimitsCount += 1
        return getTimeLimitsLogic()
    }
}
