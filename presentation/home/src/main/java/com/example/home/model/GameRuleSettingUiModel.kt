package com.example.home.model

import com.example.domainObject.game.rule.BoardHandeRule
import com.example.domainObject.game.rule.GameLogicRule
import com.example.domainObject.game.rule.Hande
import com.example.domainObject.game.rule.Turn

/**
 * ルール設定CardのUiModel
 *
 */
sealed interface GameRuleSettingUiModel {

    /**
     * カスタムしないルール
     *
     */
    sealed interface NonCustom : GameRuleSettingUiModel {

        /**
         *  選択している駒落ち設定
         */
        val selectedHande: SelectedHande

        /**
         * 通常将棋
         *
         */
        data class Normal(
            override val selectedHande: SelectedHande,
        ) : NonCustom {
            companion object {
                val INIT = Normal(
                    selectedHande = SelectedHande.Non,
                )
            }
        }

        /**
         * 王手将棋
         *
         */
        data class FirstCheck(
            override val selectedHande: SelectedHande,
        ) : NonCustom {
            companion object {
                val INIT = FirstCheck(
                    selectedHande = SelectedHande.Non,
                )
            }
        }
    }

    /**
     * カスタム将棋
     *
     * @property boardHandeRule 盤上設定
     * @property logicRule 駒落ちルール
     */
    data class Custom(
        val boardHandeRule: BoardHandeRule,
        val logicRule: GameLogicRule,
    ) : GameRuleSettingUiModel {
        companion object {
            val INIT = Custom(
                boardHandeRule = BoardHandeRule.DEFAULT,
                logicRule = GameLogicRule.DEFAULT,
            )
        }
    }

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

    companion object
}
