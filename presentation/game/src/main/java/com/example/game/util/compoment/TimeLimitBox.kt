package com.example.game.util.compoment

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.core.theme.Shogi2023Theme
import com.example.domainObject.game.game.Second
import com.example.domainObject.game.game.TimeLimit
import com.example.domainObject.game.rule.TimeLimitRule

@Composable
fun TimeLimitBox(modifier: Modifier = Modifier, timeLimit: TimeLimit) {
    Box(modifier = modifier) {
        SecondBox(second = timeLimit.remainingTime())
    }
}

@Composable
private fun SecondBox(modifier: Modifier = Modifier, second: Second) {
    Box(modifier = modifier
        .border(
            width = 2.dp,
            color = Color.DarkGray,
            shape = RoundedCornerShape(8.dp)
        ),
    ) {
        AutoSizeText(
            modifier = Modifier.fillMaxSize().align(Alignment.Center),
            text = second.toText()
        )
    }
}

private fun Second.toText(): String {
    val minute = (value / 60).toString().padStart(2, '0')
    val second = (value % 60).toString().padStart(2, '0')
    return "${minute}:${second}"
}


@Preview(showBackground = true)
@Composable
fun TimeLimitBoxPreview() {
    Shogi2023Theme {
        Surface(
            modifier = Modifier.wrapContentSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            TimeLimitBox(
                modifier = Modifier.padding(4.dp).height(40.dp).width(160.dp),
                timeLimit = TimeLimit(
                    TimeLimitRule(
                        Second(605),
                        Second(60),
                    )
                ),
            )
        }
    }
}