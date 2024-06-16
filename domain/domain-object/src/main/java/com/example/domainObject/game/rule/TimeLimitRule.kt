package com.example.domainObject.game.rule

/**
 * 持ち時間設定
 *
 */
sealed interface TimeLimitRule {

    /**
     * 持ち時間設定あり
     *
     * @property totalTime 持ち時間
     * @property byoyomi 秒読み
     */
    data class Setting(
        val totalTime: Long,
        val byoyomi: Long,
    ): TimeLimitRule

    /**
     * 持ち時間設定なし
     */
    data object None: TimeLimitRule
}
