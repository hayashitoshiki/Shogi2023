package com.example.testrepository

import com.example.domainObject.game.rule.GameRule
import com.example.repository.GameRuleRepository
import com.example.testDomainObject.rule.fake

class FakeGameRuleRepository : GameRuleRepository {

    var callSetGameRuleCount = 0
        private set
    var callGetGameRuleCount = 0
        private set

    var setGameRuleLogic: (rule: GameRule) -> Unit = { }
    var getGameRuleLogic: () -> GameRule = {
        GameRule.fake()
    }

    override fun setGameRule(rule: GameRule) {
        callSetGameRuleCount += 1
        setGameRuleLogic(rule)
    }

    override fun getGameRule(): GameRule {
        callGetGameRuleCount += 1
        return getGameRuleLogic()
    }
}
