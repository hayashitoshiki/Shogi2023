package com.example.home.compoment.card

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.domainObject.game.rule.Hande
import com.example.domainObject.game.rule.Turn
import com.example.home.R
import com.example.home.model.GameRuleSettingUiModel
import com.example.test.theme.Shogi2023Theme

@Composable
fun FirstCheckShogiSettingCard(
    modifier: Modifier = Modifier,
    selected: GameRuleSettingUiModel.SelectedHande,
    onChange: (GameRuleSettingUiModel.SelectedHande) -> Unit,
) {
    BaseNotCustomSettingBaseCard(
        modifier = modifier,
        title = stringResource(id = R.string.title_rule_first_check),
        selected = selected,
        onChange = onChange,
    )
}

@Preview(showBackground = true)
@Composable
fun FirstCheckShogiSettingCardPreview() {
    Shogi2023Theme {
        FirstCheckShogiSettingCard(
            selected = GameRuleSettingUiModel.SelectedHande(
                hande = Hande.KAKU,
                turn = Turn.Normal.Black,
            ),
            onChange = { },
        )
    }
}
