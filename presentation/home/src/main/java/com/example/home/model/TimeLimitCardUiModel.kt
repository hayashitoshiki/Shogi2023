package com.example.home.model

import com.example.domainObject.game.rule.PlayerTimeLimitRule

/**
 * 持ち時間設定CardのUiModel
 *
 * @property blackPlayerTimeLimitRule 先手の持ち時間設定
 * @property whitePlayerTimeLimitRule 後手の持ち時間設定
 */
data class TimeLimitCardUiModel(
    val blackPlayerTimeLimitRule: PlayerTimeLimitRule,
    val whitePlayerTimeLimitRule: PlayerTimeLimitRule,
) {
    companion object {
        val INIT = TimeLimitCardUiModel(
            blackPlayerTimeLimitRule = PlayerTimeLimitRule.INIT,
            whitePlayerTimeLimitRule = PlayerTimeLimitRule.INIT,
        )
    }
}
