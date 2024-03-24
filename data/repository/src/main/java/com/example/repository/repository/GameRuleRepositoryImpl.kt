package com.example.repository.repository

import com.example.entity.game.rule.BoardRule
import com.example.entity.game.rule.GameRule
import com.example.entity.game.rule.UserRule
import com.example.entity.game.rule.UsersRule
import com.example.repository.repositoryinterface.GameRuleRepository
import javax.inject.Inject

class GameRuleRepositoryImpl @Inject constructor() : GameRuleRepository {

    private var rule: GameRule = GameRule(
        boardRule = BoardRule(),
        usersRule = UsersRule(
            blackRule = UserRule(),
            whiteRule = UserRule(),
        )
    )

    override fun setGameRule(rule: GameRule) {
        this.rule = rule
    }

    override fun getGameRule(): GameRule = rule
}
