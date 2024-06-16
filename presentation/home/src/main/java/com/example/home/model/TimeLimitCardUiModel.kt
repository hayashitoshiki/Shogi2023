package com.example.home.model

import com.example.domainObject.game.rule.Second
import com.example.domainObject.game.rule.TimeLimitRule

/**
 * 持ち時間設定CardのUiModel
 *
 * @property blackTimeLimitRule 先手の持ち時間設定
 * @property whiteTimeLimitRule 後手の持ち時間設定
 */
data class TimeLimitCardUiModel(
    val blackTimeLimitRule: TimeLimitRule,
    val whiteTimeLimitRule: TimeLimitRule,
) {
    companion object {
        val INIT = TimeLimitCardUiModel(
            blackTimeLimitRule = TimeLimitRule(
                totalTime = Second(600),
                byoyomi = Second(0),
            ),
            whiteTimeLimitRule = TimeLimitRule(
                totalTime =  Second(600),
                byoyomi =  Second(0),
            ),
        )
    }
}
