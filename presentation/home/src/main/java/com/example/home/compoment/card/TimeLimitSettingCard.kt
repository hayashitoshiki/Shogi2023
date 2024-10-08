package com.example.home.compoment.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.domainObject.game.game.Seconds
import com.example.domainObject.game.rule.GameTimeLimitRule
import com.example.domainObject.game.rule.PlayerTimeLimitRule
import com.example.domainObject.game.rule.Turn
import com.example.home.R
import com.example.home.ext.turnImageRes
import com.example.home.model.TimeLimitCardUiModel
import com.example.core.theme.Shogi2023Theme

@Composable
fun TimeLimitSettingCard(
    modifier: Modifier = Modifier,
    uiModel: TimeLimitCardUiModel,
    onChangeTimeLimitTotalTime: (Turn, Seconds) -> Unit,
    onChangeTimeLimitSecond: (Turn, Seconds) -> Unit,
) {
    Card(
        modifier = modifier
            .width(256.dp)
            .shadow(8.dp, RoundedCornerShape(8.dp)),
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineMedium,
                text = stringResource(id = R.string.title_time_limit),
            )
            val turnList = listOf(
                Pair(
                    Turn.Normal.Black,
                    uiModel.timeLimitRule.blackTimeLimitRule,
                ),
                Pair(
                    Turn.Normal.White,
                    uiModel.timeLimitRule.whiteTimeLimitRule,
                ),
            )
            turnList.forEach {
                TimeLimitSettingItem(
                    turn = it.first,
                    uiModel = it.second,
                    onChangeTimeLimitTotalTime = onChangeTimeLimitTotalTime,
                    onChangeTimeLimitSecond = onChangeTimeLimitSecond,
                )
            }
        }
    }
}

@Composable
fun TimeLimitSettingItem(
    turn: Turn,
    uiModel: PlayerTimeLimitRule,
    onChangeTimeLimitTotalTime: (Turn, Seconds) -> Unit,
    onChangeTimeLimitSecond: (Turn, Seconds) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            modifier = Modifier.size(24.dp),
            painter = painterResource(turn.turnImageRes),
            contentDescription = "",
        )
        Spacer(modifier = Modifier.size(8.dp))
        Column {
            TotalTimeItem(
                selectTotalTime = uiModel.totalTime,
                onChangeHande = { onChangeTimeLimitTotalTime(turn, it) },
            )
            ByoyomiItem(
                selectSeconds = uiModel.byoyomi,
                onChangeHande = { onChangeTimeLimitSecond(turn, it) },
            )
        }
    }
}

@Composable
private fun TotalTimeItem(
    selectTotalTime: Seconds,
    onChangeHande: (Seconds) -> Unit,
) {
    RuleItem(title = stringResource(id = R.string.time_limit_totalTime)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val minits = selectTotalTime.minutes.toInt()
            val second = selectTotalTime.seconds.toInt()
            TimeSettingItem(
                label = stringResource(id = R.string.label_time_minutes),
                selectTime = minits,
                onChangeHande = { onChangeHande(Seconds.setSeconds((it * 60 + second).toLong())) },
            )
            Spacer(modifier = Modifier.size(4.dp))
            TimeSettingItem(
                label = stringResource(id = R.string.label_time_second),
                selectTime = second,
                onChangeHande = { onChangeHande(Seconds.setSeconds((minits * 60 + it).toLong())) },
            )
        }
    }
}

@Composable
private fun ByoyomiItem(
    selectSeconds: Seconds,
    onChangeHande: (Seconds) -> Unit,
) {
    RuleItem(title = stringResource(id = R.string.time_limit_byoyomi)) {
        TimeSettingItem(
            label = stringResource(id = R.string.label_time_second),
            selectTime = selectSeconds.seconds.toInt(),
            onChangeHande = { onChangeHande(Seconds.setSeconds(it.toLong())) },
        )
    }
}

@Composable
private fun TimeSettingItem(
    label: String,
    selectTime: Int,
    onChangeHande: (Int) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        TimeDropdown(
            selectSecond = selectTime,
            onChangeHande = onChangeHande,
        )
        Spacer(modifier = Modifier.size(4.dp))
        Text(
            style = MaterialTheme.typography.labelSmall,
            text = label,
        )
    }
}

@Composable
private fun RuleItem(
    title: String,
    content: @Composable () -> Unit,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            modifier = Modifier.align(Alignment.CenterStart),
            style = MaterialTheme.typography.titleSmall,
            text = title,
        )

        Box(modifier = Modifier.align(Alignment.CenterEnd)) {
            content()
        }
    }
}

private val options = buildList {
    (0..59).forEach {
        add(it)
    }
}

@Composable
private fun TimeDropdown(
    modifier: Modifier = Modifier,
    selectSecond: Int,
    onChangeHande: (Int) -> Unit,
) {
    val expanded = remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.CenterStart,
        modifier = modifier
            .size(48.dp, 28.dp)
            .clip(RoundedCornerShape(4.dp))
            .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(4.dp))
            .clickable { expanded.value = !expanded.value },
    ) {
        Text(
            text = selectSecond.toString(),
            modifier = Modifier.padding(start = 10.dp),
        )
        Icon(
            Icons.Filled.ArrowDropDown,
            "",
            Modifier.align(Alignment.CenterEnd),
        )
        DropdownMenu(
            expanded = expanded.value,
            onDismissRequest = { expanded.value = false },
        ) {
            options.forEach { selectionOption ->
                DropdownMenuItem(
                    text = {
                        Text(text = selectionOption.toString())
                    },
                    onClick = {
                        onChangeHande(selectionOption)
                        expanded.value = false
                    },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TimeLimitSettingCardPreview() {
    Shogi2023Theme {
        TimeLimitSettingCard(
            uiModel = TimeLimitCardUiModel(
                timeLimitRule = GameTimeLimitRule.INIT,
            ),
            onChangeTimeLimitSecond = { _, _ -> },
            onChangeTimeLimitTotalTime = { _, _ -> },
        )
    }
}
