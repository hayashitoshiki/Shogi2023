package com.example.repository.repositoryinterface

import com.example.entity.game.rule.PieceSetUpRule

interface GameRuleRepository {

    /**
     * ルールの一時保存
     *
     * @return 一時保持したルール）
     */
    fun getGameRule(): PieceSetUpRule.Normal

    /**
     * ルール読み込み
     *
     * @param rule 保持するルール
     */
    fun setGameRule(rule: PieceSetUpRule.Normal)

}
