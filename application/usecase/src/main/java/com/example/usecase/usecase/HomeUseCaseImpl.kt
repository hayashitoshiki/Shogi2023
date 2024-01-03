package com.example.usecase.usecase

import com.example.entity.game.rule.PieceSetUpRule
import com.example.repository.repositoryinterface.GameRuleRepository
import com.example.usecase.usecaseinterface.HomeUseCase
import javax.inject.Inject

/**
 * 将棋のビジネスロジック
 *
 */
class HomeUseCaseImpl @Inject constructor(
    private val gameRuleRepository: GameRuleRepository,
) : HomeUseCase {

    /**
     * ルール登録
     *
     * @param pieceSetUpRule ルール
     */
    override fun setGameRule(pieceSetUpRule: PieceSetUpRule.Normal) {
        gameRuleRepository.setGameRule(pieceSetUpRule)
    }
}
