package com.example.domainObject.game.rule


/**
 * 各手番のルールまとめ
 *
 * @property hande 駒落ち設定
 * @property timeLimitRule 持ち時間設定
 * @property isFirstCheckEnd 王手将棋の適用
 * @property isTryRule トライルールの適用
 */
data class PlayerRule(
    val hande: Hande = Hande.NON,
    val timeLimitRule: TimeLimitRule = TimeLimitRule.None,
    val isFirstCheckEnd: Boolean = false,
    val isTryRule: Boolean = true,
) {
    companion object
}
