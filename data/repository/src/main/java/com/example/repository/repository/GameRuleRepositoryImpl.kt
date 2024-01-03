package com.example.repository.repository

import com.example.entity.game.rule.PieceSetUpRule
import com.example.repository.repositoryinterface.GameRuleRepository
import javax.inject.Inject

class GameRuleRepositoryImpl @Inject constructor() : GameRuleRepository {
    private var rule: PieceSetUpRule.Normal = PieceSetUpRule.Normal.NoHande

    /**
     * ルール読み込み
     *
     * @return ログ保存用のキー（まだ何も保存されていない場合は新規でKeyを作成して返却）
     */
    override fun getGameRule(): PieceSetUpRule.Normal {
        return rule
    }

    /**
     * ルールの一時保存
     *
     * @param rule ログを保存するキー
     */
    override fun setGameRule(rule: PieceSetUpRule.Normal) {
        this.rule = rule
    }

}
