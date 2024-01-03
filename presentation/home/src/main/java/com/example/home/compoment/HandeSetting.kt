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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.entity.game.rule.PieceSetUpRule
import com.example.home.ext.stringRes
import com.example.home.ext.turnImageRes
import com.example.shogi2023.ui.theme.Shogi2023Theme

@Composable
fun HandeSettingBox(selected: MutableState<PieceSetUpRule.Normal>) {
    Column(modifier = Modifier.padding(8.dp)) {
        HandeFilterChip(selected, PieceSetUpRule.Normal.NoHande)
        Row {
            HandeFilterChip(selected, PieceSetUpRule.Normal.BlackHandeHisya)
            Spacer(modifier = Modifier.size(8.dp))
            HandeFilterChip(selected, PieceSetUpRule.Normal.BlackHandeKaku)
            Spacer(modifier = Modifier.size(8.dp))
            HandeFilterChip(selected, PieceSetUpRule.Normal.BlackHandeHisyaKaku)
        }
        Row {
            HandeFilterChip(selected, PieceSetUpRule.Normal.BlackHandeFor)
            Spacer(modifier = Modifier.size(8.dp))
            HandeFilterChip(selected, PieceSetUpRule.Normal.BlackHandeSix)
            Spacer(modifier = Modifier.size(8.dp))
            HandeFilterChip(selected, PieceSetUpRule.Normal.BlackHandeEight)
        }
        Row {
            HandeFilterChip(selected, PieceSetUpRule.Normal.WhiteHandeHisya)
            Spacer(modifier = Modifier.size(8.dp))
            HandeFilterChip(selected, PieceSetUpRule.Normal.WhiteHandeKaku)
            Spacer(modifier = Modifier.size(8.dp))
            HandeFilterChip(selected, PieceSetUpRule.Normal.WhiteHandeHisyaKaku)
        }
        Row {
            HandeFilterChip(selected, PieceSetUpRule.Normal.WhiteHandeFor)
            Spacer(modifier = Modifier.size(8.dp))
            HandeFilterChip(selected, PieceSetUpRule.Normal.WhiteHandeSix)
            Spacer(modifier = Modifier.size(8.dp))
            HandeFilterChip(selected, PieceSetUpRule.Normal.WhiteHandeEight)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HandeFilterChip(
    selected: MutableState<PieceSetUpRule.Normal>,
    pieceSetUpRule: PieceSetUpRule.Normal
) {
    FilterChip(
        modifier = Modifier.width(88.dp),
        selected = selected.value == pieceSetUpRule,
        onClick = { selected.value = pieceSetUpRule },
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
                    fontSize = 11.sp,
                    text = stringResource(id = pieceSetUpRule.stringRes),
                )
            }

        }
    )
}

@Preview(showBackground = true)
@Composable
fun HandeSettingBoxPreview() {
    val selected = remember { mutableStateOf<PieceSetUpRule.Normal>(PieceSetUpRule.Normal.NoHande) }

    Shogi2023Theme {
        HandeSettingBox(
            selected = selected
        )
    }
}
