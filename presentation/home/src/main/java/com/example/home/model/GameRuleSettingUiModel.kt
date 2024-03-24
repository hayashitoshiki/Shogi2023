package com.example.home.model

import com.example.entity.game.rule.Hande
import com.example.entity.game.rule.Turn

/**
 * ルール設定CardのUiModel
 *
 */
sealed interface GameRuleSettingUiModel {

    /**
     * 駒落ち
     */
    val selectedHande: SelectedHande

    /**
     * 通常将棋
     */
    data class Normal(
        override val selectedHande: SelectedHande = SelectedHande.Non
    ) : GameRuleSettingUiModel

    /**
     * 王手将棋
     */
    data class FirstCheck(
        override val selectedHande: SelectedHande = SelectedHande.Non
    ) : GameRuleSettingUiModel

    /**
     * 選択してるハンデ
     *
     * @property hande 駒落ち
     * @property turn 手番
     */
    data class SelectedHande(
        val hande: Hande,
        val turn: Turn,
    ) {
        companion object {
            val Non = SelectedHande(
                hande = Hande.NON,
                turn = Turn.Normal.Black,
            )
        }
    }
}
