package com.example.repository

import com.example.domainObject.game.rule.GameRule
import javax.inject.Inject

class GameRuleRepositoryImpl @Inject constructor() : GameRuleRepository {

    private var rule: GameRule = GameRule.DEFAULT

    override fun set(rule: GameRule) {
        this.rule = rule
    }

    override fun get(): GameRule = rule
}
