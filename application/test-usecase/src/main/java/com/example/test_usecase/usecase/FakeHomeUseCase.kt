package com.example.test_usecase.usecase

import com.example.domainObject.game.rule.GameRule
import com.example.usecase.usecaseinterface.HomeUseCase

class FakeHomeUseCase(): HomeUseCase {

    var calSsetGameRuleCount = 0
        private set

    var setGameRuleLogic: (GameRule) -> Unit = { }

    override fun setGameRule(rule: GameRule) {
        calSsetGameRuleCount += 1
        setGameRuleLogic(rule)
    }
}
