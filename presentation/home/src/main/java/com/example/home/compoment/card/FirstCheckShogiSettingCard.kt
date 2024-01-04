package com.example.home.compoment.card

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.entity.game.rule.PieceSetUpRule
import com.example.home.R

@Composable
fun FirstCheckShogiSettingCard(
    modifier: Modifier = Modifier,
    selected: PieceSetUpRule.Normal,
    onChange: (PieceSetUpRule.Normal) -> Unit,
) {
    BaseSettingCard(
        modifier = modifier,
        title = stringResource(id = R.string.title_rule_first_check),
        selected = selected,
        onChange = onChange,
    )
}
