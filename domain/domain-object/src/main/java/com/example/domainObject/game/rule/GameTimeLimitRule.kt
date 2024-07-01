package com.example.domainObject.game.rule

/**
 * 対局の持ち時間設定
 *
 * @property blackTimeLimitRule 先手の持ち時間設定
 * @property whiteTimeLimitRule 後手の持ち時間設定
 */
data class GameTimeLimitRule(
    val blackTimeLimitRule: PlayerTimeLimitRule,
    val whiteTimeLimitRule: PlayerTimeLimitRule,
) {
    companion object {
        val INIT = GameTimeLimitRule(
            blackTimeLimitRule = PlayerTimeLimitRule.INIT,
            whiteTimeLimitRule = PlayerTimeLimitRule.INIT,
        )
    }
}
