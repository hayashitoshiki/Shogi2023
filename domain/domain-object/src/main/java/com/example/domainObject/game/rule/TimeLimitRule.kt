package com.example.domainObject.game.rule

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
            totalTime = Second(600),
            byoyomi = Second(0),
        )
    }
}

/**
 * 秒数
 */
data class Second(val value: Int)
