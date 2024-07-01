package com.example.domainObject.game.rule

import com.example.domainObject.game.game.Seconds

/**
 * プレイヤーの持ち時間設定
 *
 * @property totalTime 持ち時間
 * @property byoyomi 秒読み
 */
data class PlayerTimeLimitRule(
    val totalTime: Seconds,
    val byoyomi: Seconds,
) {
    companion object {
        val INIT = PlayerTimeLimitRule(
            totalTime = Seconds.setMinutes(10),
            byoyomi = Seconds.ZERO,
        )
    }
}
