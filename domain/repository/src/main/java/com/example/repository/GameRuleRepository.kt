package com.example.repository

import com.example.domainObject.game.rule.GameRule

interface GameRuleRepository {

    /**
     * ルール読み込み
     *
     * @param rule 保持するルール
     */
    fun set(rule: GameRule)

    /**
     * ルールの一時保存
     *
     * @return 一時保持したルール
     */
    fun get(): GameRule
}
