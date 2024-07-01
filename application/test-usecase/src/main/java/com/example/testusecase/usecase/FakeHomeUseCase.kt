package com.example.testusecase.usecase

import com.example.domainObject.game.rule.GameRule
import com.example.usecaseinterface.usecase.HomeUseCase

class FakeHomeUseCase() : HomeUseCase {

    var calSsetGameRuleCount = 0
        private set

    var setGameRuleLogic: (GameRule) -> Unit = { }

    override fun setGameRule(rule: GameRule) {
        calSsetGameRuleCount += 1
        setGameRuleLogic(rule)
    }
}
