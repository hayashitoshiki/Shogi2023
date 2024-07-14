package com.example.usecase

import com.example.domainObject.game.rule.GameRule
import com.example.domainObject.game.rule.GameTimeLimitRule
import com.example.repository.GameRuleRepository
import com.example.repository.GameTimeLimitRuleRepository
import com.example.usecaseinterface.usecase.HomeUseCase
import javax.inject.Inject

class HomeUseCaseImpl @Inject constructor(
    private val gameRuleRepository: GameRuleRepository,
    private val gameTimeLimitRuleRepository: GameTimeLimitRuleRepository,
) : HomeUseCase {

    override suspend fun getTimeLimits(): GameTimeLimitRule {
        return gameTimeLimitRuleRepository.get()
    }

    override suspend fun setGameRule(rule: GameRule) {
        gameTimeLimitRuleRepository.set(rule.timeLimitRule)
        gameRuleRepository.set(rule)
    }
}
