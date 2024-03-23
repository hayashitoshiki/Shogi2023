package com.example.entity.game.rule


/**
 * 各手番のルール
 *
 * @property hande 駒落ち設定
 * @property isFirstCheckEnd 王手将棋の適用
 */
data class UserRule(
    val hande: Hande = Hande.NON,
    val isFirstCheckEnd: Boolean = false,
)