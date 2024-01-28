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
import com.example.entity.game.rule.PieceSetUpRule
import com.example.home.ext.stringRes
import com.example.home.ext.turnImageRes
import com.example.core.theme.Shogi2023Theme

@Composable
fun HandeSettingBox(
    selected: PieceSetUpRule.Normal,
    onChange: (PieceSetUpRule.Normal) -> Unit,
) {
    Column(modifier = Modifier.padding(8.dp)) {
        HandeFilterChip(
            selected = selected,
            onChange = onChange,
            pieceSetUpRule = PieceSetUpRule.Normal.NoHande,
        )
        Row {
            HandeFilterChip(
                selected = selected,
                onChange = onChange,
                pieceSetUpRule = PieceSetUpRule.Normal.BlackHandeHisya,
            )
            Spacer(modifier = Modifier.size(8.dp))
            HandeFilterChip(
                selected = selected,
                onChange = onChange,
                pieceSetUpRule = PieceSetUpRule.Normal.BlackHandeKaku,
            )
            Spacer(modifier = Modifier.size(8.dp))
            HandeFilterChip(
                selected = selected,
                onChange = onChange,
                pieceSetUpRule = PieceSetUpRule.Normal.BlackHandeHisyaKaku,
            )
        }
        Row {
            HandeFilterChip(
                selected = selected,
                onChange = onChange,
                pieceSetUpRule = PieceSetUpRule.Normal.BlackHandeFor,
            )
            Spacer(modifier = Modifier.size(8.dp))
            HandeFilterChip(
                selected = selected,
                onChange = onChange,
                pieceSetUpRule = PieceSetUpRule.Normal.BlackHandeSix,
            )
            Spacer(modifier = Modifier.size(8.dp))
            HandeFilterChip(
                selected = selected,
                onChange = onChange,
                pieceSetUpRule = PieceSetUpRule.Normal.BlackHandeEight,
                )
        }
        Row {
            HandeFilterChip(
                selected = selected,
                onChange = onChange,
                pieceSetUpRule = PieceSetUpRule.Normal.WhiteHandeHisya,
            )
            Spacer(modifier = Modifier.size(8.dp))
            HandeFilterChip(
                selected = selected,
                onChange = onChange,
                pieceSetUpRule = PieceSetUpRule.Normal.WhiteHandeKaku,
            )
            Spacer(modifier = Modifier.size(8.dp))
            HandeFilterChip(
                selected = selected,
                onChange = onChange,
                pieceSetUpRule = PieceSetUpRule.Normal.WhiteHandeHisyaKaku,
            )
        }
        Row {
            HandeFilterChip(
                selected = selected,
                onChange = onChange,
                pieceSetUpRule = PieceSetUpRule.Normal.WhiteHandeFor,
            )
            Spacer(modifier = Modifier.size(8.dp))
            HandeFilterChip(
                selected = selected,
                onChange = onChange,
                pieceSetUpRule = PieceSetUpRule.Normal.WhiteHandeSix,
            )
            Spacer(modifier = Modifier.size(8.dp))
            HandeFilterChip(
                selected = selected,
                onChange = onChange,
                pieceSetUpRule = PieceSetUpRule.Normal.WhiteHandeEight,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HandeFilterChip(
    selected: PieceSetUpRule.Normal,
    onChange: (PieceSetUpRule.Normal) -> Unit,
    pieceSetUpRule: PieceSetUpRule.Normal
) {
    FilterChip(
        modifier = Modifier.width(88.dp),
        selected = selected == pieceSetUpRule,
        onClick = { onChange(pieceSetUpRule) },
        label = {
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                pieceSetUpRule.turnImageRes?.let { turnImageRes ->
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

        }
    )
}

@Preview(showBackground = true)
@Composable
fun HandeSettingBoxPreview() {
    Shogi2023Theme {
        HandeSettingBox(
            selected = PieceSetUpRule.Normal.BlackHandeKaku,
            onChange = { },
        )
    }
}