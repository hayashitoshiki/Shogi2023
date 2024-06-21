package com.example.domainObject.game.game

import com.example.domainObject.game.rule.TimeLimitRule

/**
 * 対局中の持ち時間
 *
 * @property setting 持ち時間設定
 * @property totalTime 残りの持ち時間
 * @property byoyomi 秒読み
 */
data class TimeLimit(
    val setting: TimeLimitRule,
    val totalTime: Second,
    val byoyomi: Second,
) {
    constructor(timeLimitRule: TimeLimitRule) : this(
        setting = timeLimitRule,
        totalTime = timeLimitRule.totalTime,
        byoyomi = timeLimitRule.byoyomi,
    )

    /**
     * 残り時間を取得
     *
     * @return 残り時間
     */
    fun remainingTime(): Second {
        return if(totalTime != Second(0)) {
            totalTime
        } else {
            byoyomi
        }
    }

    companion object {
        val INIT = TimeLimit(
            setting = TimeLimitRule.INIT,
            totalTime = Second(0),
            byoyomi = Second(0),
        )
    }
}
