package com.example.repository.repository

import com.example.domainObject.game.rule.BoardRule
import com.example.domainObject.game.rule.GameRule
import com.example.domainObject.game.rule.PlayerRule
import com.example.domainObject.game.rule.PlayersRule
import com.example.repository.repositoryinterface.GameRuleRepository
import javax.inject.Inject

class GameRuleRepositoryImpl @Inject constructor() : GameRuleRepository {

    private var rule: GameRule = GameRule(
        boardRule = BoardRule(),
        playersRule = PlayersRule(
            blackRule = PlayerRule(),
            whiteRule = PlayerRule(),
        )
    )

    override fun setGameRule(rule: GameRule) {
        this.rule = rule
    }

    override fun getGameRule(): GameRule = rule
}
