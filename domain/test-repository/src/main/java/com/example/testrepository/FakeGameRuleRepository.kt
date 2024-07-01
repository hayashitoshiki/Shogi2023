package com.example.testrepository

import com.example.domainObject.game.rule.BoardRule
import com.example.domainObject.game.rule.GameRule
import com.example.domainObject.game.rule.PlayerRule
import com.example.domainObject.game.rule.PlayersRule
import com.example.repository.GameRuleRepository

class FakeGameRuleRepository : GameRuleRepository {

    var callSetGameRuleCount = 0
        private set
    var callGetGameRuleCount = 0
        private set

    var setGameRuleLogic: (rule: GameRule) -> Unit = { }
    var getGameRuleLogic: () -> GameRule = {
        GameRule(
            boardRule = BoardRule(),
            playersRule = PlayersRule(
                blackRule = PlayerRule(),
                whiteRule = PlayerRule(),
            ),
        )
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
