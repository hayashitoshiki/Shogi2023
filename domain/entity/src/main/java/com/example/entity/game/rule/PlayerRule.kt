package com.example.entity.game.rule


/**
 * 各手番のルールまとめ
 *
 * @property hande 駒落ち設定
 * @property isFirstCheckEnd 王手将棋の適用
 */
data class PlayerRule(
    val hande: Hande = Hande.NON,
    val isFirstCheckEnd: Boolean = false,
)