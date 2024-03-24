package com.example.repository.repositoryinterface

import com.example.entity.game.rule.GameRule

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
     * @return 一時保持したルール
     */
    fun getGameRule(): GameRule
}
