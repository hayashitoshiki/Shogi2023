package com.example.domainObject.game.game

import com.example.domainObject.game.rule.PlayerTimeLimitRule

/**
 * 対局中の持ち時間
 *
 * @property setting 持ち時間設定
 * @property totalTime 残りの持ち時間
 * @property byoyomi 秒読み
 */
data class TimeLimit(
    val setting: PlayerTimeLimitRule,
    val totalTime: Seconds,
    val byoyomi: Seconds,
) {
    constructor(playerTimeLimitRule: PlayerTimeLimitRule) : this(
        setting = playerTimeLimitRule,
        totalTime = playerTimeLimitRule.totalTime,
        byoyomi = playerTimeLimitRule.byoyomi,
    )

    /**
     * 残り時間を取得
     */
    val remainingTime: Seconds = if (totalTime != Seconds.ZERO) {
        totalTime
    } else {
        byoyomi
    }

    companion object {
        val INIT = TimeLimit(
            setting = PlayerTimeLimitRule.INIT,
            totalTime = Seconds.setMinutes(10),
            byoyomi = Seconds.ZERO,
        )
    }
}
