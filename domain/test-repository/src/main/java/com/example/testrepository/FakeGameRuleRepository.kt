package com.example.testrepository

import com.example.domainObject.game.rule.GameRule
import com.example.repository.GameRuleRepository
import com.example.testDomainObject.rule.fake

class FakeGameRuleRepository : GameRuleRepository {

    var callSetCount = 0
        private set
    var callGetCount = 0
        private set

    var setLogic: (rule: GameRule) -> Unit = { }
    var getLogic: () -> GameRule = {
        GameRule.fake()
    }

    override fun set(rule: GameRule) {
        callSetCount += 1
        setLogic(rule)
    }

    override fun get(): GameRule {
        callGetCount += 1
        return getLogic()
    }
}
