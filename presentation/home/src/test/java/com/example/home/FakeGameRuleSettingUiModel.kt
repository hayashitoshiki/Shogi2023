package com.example.home

import com.example.domainObject.game.rule.Hande
import com.example.domainObject.game.rule.PlayersRule
import com.example.domainObject.game.rule.Turn
import com.example.home.model.GameRuleSettingUiModel
import com.example.testDomainObject.rule.fake

fun GameRuleSettingUiModel.Companion.fakeList(
    list: List<GameRuleSettingUiModel> = listOf(
        GameRuleSettingUiModel.NonCustom.Normal.fake(),
        GameRuleSettingUiModel.NonCustom.FirstCheck.fake(),
        GameRuleSettingUiModel.Custom.fake(),
    ),
) = list

fun GameRuleSettingUiModel.NonCustom.Normal.Companion.fake(
    selectedHande: GameRuleSettingUiModel.SelectedHande = GameRuleSettingUiModel.SelectedHande.fake(),
) = GameRuleSettingUiModel.NonCustom.Normal(
    selectedHande = selectedHande,
)

fun GameRuleSettingUiModel.NonCustom.FirstCheck.Companion.fake(
    selectedHande: GameRuleSettingUiModel.SelectedHande = GameRuleSettingUiModel.SelectedHande.fake(),
) = GameRuleSettingUiModel.NonCustom.FirstCheck(
    selectedHande = selectedHande,
)

fun GameRuleSettingUiModel.Custom.Companion.fake(
    playersRule: PlayersRule = PlayersRule.fake(),
) = GameRuleSettingUiModel.Custom(
    playersRule = playersRule,
)

fun GameRuleSettingUiModel.SelectedHande.Companion.fake(
    hande: Hande = Hande.NON,
    turn: Turn = Turn.Normal.Black,
): GameRuleSettingUiModel.SelectedHande {
    return GameRuleSettingUiModel.SelectedHande(
        hande = hande,
        turn = turn,
    )
}
