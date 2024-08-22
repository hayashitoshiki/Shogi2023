package com.example.home.model

import com.example.domainObject.game.rule.GameTimeLimitRule

/**
 * 持ち時間設定CardのUiModel
 *
 * @property timeLimitRule 持ち時間設定情報
 */
data class TimeLimitCardUiModel(
    val timeLimitRule: GameTimeLimitRule,
) {
    companion object {
        val INIT = TimeLimitCardUiModel(
            timeLimitRule = GameTimeLimitRule.INIT,
        )
    }
}
