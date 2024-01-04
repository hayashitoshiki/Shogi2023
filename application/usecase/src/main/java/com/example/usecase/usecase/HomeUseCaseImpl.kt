package com.example.usecase.usecase

import com.example.entity.game.rule.GameRule
import com.example.repository.repositoryinterface.GameRuleRepository
import com.example.usecase.usecaseinterface.HomeUseCase
import javax.inject.Inject

class HomeUseCaseImpl @Inject constructor(
    private val gameRuleRepository: GameRuleRepository,
) : HomeUseCase {

    override fun setGameRule(rule: GameRule) {
        gameRuleRepository.setGameRule(rule)
    }
}
