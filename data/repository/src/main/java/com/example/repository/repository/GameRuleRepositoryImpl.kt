package com.example.repository.repository

import com.example.entity.game.rule.GameRule
import com.example.repository.repositoryinterface.GameRuleRepository
import javax.inject.Inject

class GameRuleRepositoryImpl @Inject constructor() : GameRuleRepository {

    private var rule: GameRule = GameRule()

    override fun setGameRule(rule: GameRule) {
        this.rule = rule
    }

    override fun getGameRule(): GameRule = rule

    override fun getIsFirstCheckEndRule(): Boolean = rule.isFirstCheckEnd
}
