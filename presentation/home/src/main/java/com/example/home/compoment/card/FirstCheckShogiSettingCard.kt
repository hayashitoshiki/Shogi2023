package com.example.home.compoment.card

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.home.R
import com.example.home.model.GameRuleSettingUiModel

@Composable
fun FirstCheckShogiSettingCard(
    modifier: Modifier = Modifier,
    selected: GameRuleSettingUiModel.SelectedHande,
    onChange: (GameRuleSettingUiModel.SelectedHande) -> Unit,
) {
    BaseSettingCard(
        modifier = modifier,
        title = stringResource(id = R.string.title_rule_first_check),
        selected = selected,
        onChange = onChange,
    )
}
