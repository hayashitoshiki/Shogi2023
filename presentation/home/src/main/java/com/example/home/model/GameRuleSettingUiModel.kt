package com.example.home.model

import com.example.entity.game.rule.PieceSetUpRule

/**
 * ルール設定CardのUiModel
 *
 */
sealed interface GameRuleSettingUiModel {

    /**
     * 駒落ち
     */
    val pieceHande: PieceSetUpRule.Normal

    /**
     * 通常将棋
     */
    data class Normal(
        override val pieceHande: PieceSetUpRule.Normal = PieceSetUpRule.Normal.NoHande
    ) : GameRuleSettingUiModel

    /**
     * 王手将棋
     */
    data class FirstCheck(
        override val pieceHande: PieceSetUpRule.Normal = PieceSetUpRule.Normal.NoHande
    ) : GameRuleSettingUiModel
}
