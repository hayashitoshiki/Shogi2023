package com.example.home.compoment.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp

@Composable
fun BaseSettingCard(
    modifier: Modifier = Modifier,
    title: String,
    content: @Composable () -> Unit,
) {
    Card(modifier = modifier.shadow(8.dp, RoundedCornerShape(8.dp))) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineMedium,
                text = title,
            )
            Spacer(modifier = Modifier.size(16.dp))
            content()
        }
    }
}
