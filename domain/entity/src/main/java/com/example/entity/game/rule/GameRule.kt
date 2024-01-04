package com.example.entity.game.rule

/**
 * 対局ルール
 *
 * @property pieceSetUpRule 駒落ち設定
 * @property isFirstCheckEnd 王手将棋が適用されているか
 */
data class GameRule(
    val pieceSetUpRule: PieceSetUpRule.Normal = PieceSetUpRule.Normal.NoHande,
    val isFirstCheckEnd: Boolean = false,
)
