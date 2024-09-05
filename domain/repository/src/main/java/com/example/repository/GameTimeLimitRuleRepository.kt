package com.example.repository

import com.example.domainObject.game.rule.GameTimeLimitRule

/**
 * 対局の持ち時間設定用Repository
 */
interface GameTimeLimitRuleRepository {

    /**
     * 保存
     *
     * @param gameTimeLimitRule 対局の持ち時間設定
     */
    suspend fun set(gameTimeLimitRule: GameTimeLimitRule)

    /**
     * 取得
     *
     * @return 対局の持ち時間設定
     */
    suspend fun get(): GameTimeLimitRule
}
