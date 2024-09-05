package com.example.repository

import com.example.domainObject.game.rule.GameRule

/**
 * 対局ルール用Repository
 */
interface GameRuleRepository {

    /**
     * 保存
     *
     * @param rule 対局ルール
     */
    fun set(rule: GameRule)

    /**
     * 取得
     *
     * @return 対局ルール
     */
    fun get(): GameRule
}
