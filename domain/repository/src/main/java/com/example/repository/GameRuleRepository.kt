package com.example.repository

import com.example.domainObject.game.rule.GameRule

interface GameRuleRepository {

    /**
     * ルールの一時保存
     *
     * @param rule 保持するルール
     */
    fun set(rule: GameRule)

    /**
     * ルール読み込み
     *
     * @return 一時保持したルール
     */
    fun get(): GameRule
}
