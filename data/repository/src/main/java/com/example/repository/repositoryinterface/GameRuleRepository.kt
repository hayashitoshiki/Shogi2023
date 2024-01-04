package com.example.repository.repositoryinterface

import com.example.entity.game.rule.GameRule
import com.example.entity.game.rule.PieceSetUpRule

interface GameRuleRepository {

    /**
     * ルール読み込み
     *
     * @param rule 保持するルール
     */
    fun setGameRule(rule: GameRule)

    /**
     * ルールの一時保存
     *
     * @return 一時保持したルール）
     */
    fun getGameRule(): PieceSetUpRule.Normal

    /**
     * 王手将棋の有無取得
     *
     * @return 王手将棋の有無設定値
     */
    fun getIsFirstCheckEndRule(): Boolean
}
