package com.example.home.compoment

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domainObject.game.rule.Hande
import com.example.domainObject.game.rule.Turn
import com.example.home.ext.stringRes
import com.example.home.ext.turnImageRes
import com.example.home.model.GameRuleSettingUiModel
import com.example.test.theme.Shogi2023Theme

@Composable
fun HandeSettingBox(
    selected: GameRuleSettingUiModel.SelectedHande,
    onChange: (GameRuleSettingUiModel.SelectedHande) -> Unit,
) {
    Column(modifier = Modifier.padding(8.dp)) {
        HandeFilterChip(
            selected = selected,
            onChange = onChange,
            pieceSetUpRule = Hande.NON,
            turn = Turn.Normal.Black,
        )
        val turnList = listOf(Turn.Normal.Black, Turn.Normal.White)
        val handeList = listOf(
            Triple(Hande.HISYA, Hande.KAKU, Hande.TWO),
            Triple(Hande.FOR, Hande.SIX, Hande.EIGHT),
        )
        turnList.forEach { turn ->
            handeList.forEach {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    HandeFilterChip(
                        selected = selected,
                        onChange = onChange,
                        pieceSetUpRule = it.first,
                        turn = turn,
                    )
                    HandeFilterChip(
                        selected = selected,
                        onChange = onChange,
                        pieceSetUpRule = it.second,
                        turn = turn,
                    )
                    HandeFilterChip(
                        selected = selected,
                        onChange = onChange,
                        pieceSetUpRule = it.third,
                        turn = turn,
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HandeFilterChip(
    selected: GameRuleSettingUiModel.SelectedHande,
    onChange: (GameRuleSettingUiModel.SelectedHande) -> Unit,
    pieceSetUpRule: Hande,
    turn: Turn,
) {
    FilterChip(
        modifier = Modifier.width(88.dp),
        selected = selected.hande == pieceSetUpRule && selected.turn == turn,
        onClick = { onChange(GameRuleSettingUiModel.SelectedHande(pieceSetUpRule, turn)) },
        label = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                turn.turnImageRes.let { turnImageRes ->
                    Image(
                        modifier = Modifier.size(12.dp),
                        painter = painterResource(turnImageRes),
                        contentDescription = "",
                    )
                    Spacer(modifier = Modifier.padding(2.dp))
                }
                Text(
                    style = MaterialTheme.typography.labelSmall,
                    text = stringResource(id = pieceSetUpRule.stringRes),
                )
            }
        },
    )
}

@Preview(showBackground = true)
@Composable
fun HandeSettingBoxPreview() {
    Shogi2023Theme {
        HandeSettingBox(
            selected = GameRuleSettingUiModel.SelectedHande(
                hande = Hande.KAKU,
                turn = Turn.Normal.Black,
            ),
            onChange = { },
        )
    }
}
