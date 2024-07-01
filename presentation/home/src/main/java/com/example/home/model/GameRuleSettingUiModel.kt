package com.example.home.model

import com.example.domainObject.game.rule.Hande
import com.example.domainObject.game.rule.PlayerRule
import com.example.domainObject.game.rule.PlayersRule
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
         *  駒落ち
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
     * @property playersRule 駒落ち
     */
    data class Custom(
        val playersRule: PlayersRule = PlayersRule.INIT,
    ) : GameRuleSettingUiModel {
        companion object {
            val INIT = Custom(
                playersRule = PlayersRule(
                    blackRule = PlayerRule(),
                    whiteRule = PlayerRule(),
                ),
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
