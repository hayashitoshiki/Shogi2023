package com.example.home.compoment.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.theme.Shogi2023Theme
import com.example.domainObject.game.rule.Hande
import com.example.domainObject.game.rule.PlayerRule
import com.example.domainObject.game.rule.PlayersRule
import com.example.domainObject.game.rule.Turn
import com.example.game.util.extension.stringRes
import com.example.home.R
import com.example.home.ext.stringRes
import com.example.home.model.GameRuleSettingUiModel

@Composable
fun CustomShogiSettingCard(
    modifier: Modifier = Modifier,
    custom: GameRuleSettingUiModel.Custom,
    onChangeFirstCheck: (Turn, Boolean) -> Unit,
    onChangeHande: (GameRuleSettingUiModel.SelectedHande) -> Unit,
) {
    val turnList = listOf(
        Pair(
            Turn.Normal.Black,
            custom.playersRule.blackRule
        ),
        Pair(
            Turn.Normal.White,
            custom.playersRule.whiteRule
        ),
    )

    BaseSettingCard(
        modifier = modifier,
        title = stringResource(id = R.string.title_rule_custom),
    ) {
        turnList.forEach {
            PlayerSettingItem(
                turn = it.first,
                rule = it.second,
                onChangeFirstCheck = onChangeFirstCheck,
                onChangeHande = onChangeHande,
            )
        }
    }
}

@Composable
private fun PlayerSettingItem(
    turn: Turn,
    rule: PlayerRule,
    onChangeFirstCheck: (Turn, Boolean) -> Unit,
    onChangeHande: (GameRuleSettingUiModel.SelectedHande) -> Unit,
) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            style = MaterialTheme.typography.titleMedium,
            text = stringResource(id = turn.stringRes),
        )
        SwitchRuleItem(
            title = stringResource(id = R.string.title_rule_first_check),
            checked = rule.isFirstCheckEnd,
            onCheckedChange = { onChangeFirstCheck(turn, !rule.isFirstCheckEnd) },
        )
        HandeRuleItem(
            selectedHande = rule.hande,
            onChangeHande = { hande ->
                onChangeHande(
                    GameRuleSettingUiModel.SelectedHande(
                        hande = hande,
                        turn = turn,
                    )
                )
            },
        )
    }
}

@Composable
private fun SwitchRuleItem(
    title: String,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit),
) {
    RuleItem(title = title) {
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}

@Composable
private fun HandeRuleItem(
    selectedHande: Hande,
    onChangeHande: (Hande) -> Unit,
) {
    RuleItem(title = stringResource(id = R.string.title_hande)) {
        HandeDropdown(
            selectedHande = selectedHande,
            onChangeHande = onChangeHande,
        )
    }
}

@Composable
private fun RuleItem(
    title: String,
    content: @Composable () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            style = MaterialTheme.typography.titleMedium,
            text = title,
        )

        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
            content()
        }
    }
}

@Composable
private fun HandeDropdown(
    modifier: Modifier = Modifier,
    selectedHande: Hande,
    onChangeHande: (Hande) -> Unit,
) {
    val options = Hande.values().map { it }.toList()
    val expanded = remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .size(120.dp, 50.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(4.dp))
            .clickable { expanded.value = !expanded.value },
    ) {
        Text(
            text = stringResource(id = selectedHande.stringRes),
            modifier = Modifier.padding(start = 10.dp)
        )
        Icon(
            Icons.Filled.ArrowDropDown, "contentDescription",
            Modifier.align(Alignment.CenterEnd)
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false }
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(selectionOption.stringRes))
                    },
                    onClick = {
                        onChangeHande(selectionOption)
                        expanded.value = false
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomShogiSettingCardPreview() {
    Shogi2023Theme {
        CustomShogiSettingCard(
            custom = GameRuleSettingUiModel.Custom(
                playersRule = PlayersRule(
                    blackRule = PlayerRule(),
                    whiteRule = PlayerRule(),
                )
            ),
            onChangeFirstCheck = { _, _ -> },
            onChangeHande = { },
        )
    }
}
