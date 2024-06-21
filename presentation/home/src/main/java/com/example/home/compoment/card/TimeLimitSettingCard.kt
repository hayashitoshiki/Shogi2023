package com.example.home.compoment.card

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.domainObject.game.game.Second
import com.example.domainObject.game.rule.TimeLimitRule
import com.example.domainObject.game.rule.Turn
import com.example.game.util.extension.stringRes
import com.example.home.R
import com.example.home.model.TimeLimitCardUiModel

@Composable
fun TimeLimitSettingCard(
    modifier: Modifier = Modifier,
    uiModel: TimeLimitCardUiModel,
    onChangeTimeLimitTotalTime: (Turn, Second) -> Unit,
    onChangeTimeLimitSecond: (Turn, Second) -> Unit,
) {
    Card(modifier = modifier.shadow(8.dp, RoundedCornerShape(8.dp))) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineMedium,
                text = stringResource(id = R.string.title_time_limit),
            )
            val turnList = listOf(
                Pair(
                    Turn.Normal.Black,
                    uiModel.blackTimeLimitRule,
                ),
                Pair(
                    Turn.Normal.White,
                    uiModel.whiteTimeLimitRule,
                )
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
    uiModel: TimeLimitRule,
    onChangeTimeLimitTotalTime: (Turn, Second) -> Unit,
    onChangeTimeLimitSecond: (Turn, Second) -> Unit,
) {
    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.titleMedium,
            text = stringResource(id = turn.stringRes),
        )
        TotalTimeItem(
            selectTotalTime = uiModel.totalTime,
            onChangeHande = { onChangeTimeLimitTotalTime(turn, it) },
        )
        ByoyomiItem(
            selectSecond = uiModel.byoyomi,
            onChangeHande = { onChangeTimeLimitSecond(turn, it) },
        )
    }
}

@Composable
private fun TotalTimeItem(
    selectTotalTime: Second,
    onChangeHande: (Second) -> Unit,
) {
    RuleItem(title = stringResource(id = R.string.time_limit_totalTime)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            val minits = selectTotalTime.value / 60
            val second = selectTotalTime.value % 60
            TimeSettingItem(
                label =  stringResource(id = R.string.label_time_minutes),
                selectTime = minits,
                onChangeHande = { onChangeHande(Second(it * 60 + second)) },
            )
            Spacer(modifier = Modifier.size(4.dp))
            TimeSettingItem(
                label = stringResource(id = R.string.label_time_second),
                selectTime = second,
                onChangeHande = { onChangeHande(Second(minits * 60 + it)) },
            )
        }
    }
}


@Composable
private fun ByoyomiItem(
    selectSecond: Second,
    onChangeHande: (Second) -> Unit,
) {
    RuleItem(title = stringResource(id = R.string.time_limit_byoyomi)) {
        TimeSettingItem(
            label =  stringResource(id = R.string.label_time_minutes),
            selectTime = selectSecond.value,
            onChangeHande = { onChangeHande(Second(it)) },
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
    content: @Composable () -> Unit
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

val options = buildList {
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
                        Text(text = selectionOption.toString())
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
