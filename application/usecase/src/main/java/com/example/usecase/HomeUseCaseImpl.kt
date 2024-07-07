package com.example.usecase

import com.example.domainObject.game.rule.GameRule
import com.example.repository.GameRuleRepository
import com.example.usecaseinterface.usecase.HomeUseCase
import javax.inject.Inject

class HomeUseCaseImpl @Inject constructor(
    private val gameRuleRepository: GameRuleRepository,
) : HomeUseCase {

    override fun setGameRule(rule: GameRule) {
        gameRuleRepository.set(rule)
    }
}
