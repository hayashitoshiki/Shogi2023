package com.example.domainObject.game.rule

import com.example.domainObject.game.game.Second

/**
 * 持ち時間設定
 *
 * @property totalTime 持ち時間
 * @property byoyomi 秒読み
 */
data class TimeLimitRule(
    val totalTime: Second,
    val byoyomi: Second,
) {
    companion object {
        val INIT = TimeLimitRule(
            totalTime = Second(600000),
            byoyomi = Second(0),
        )
    }
}
