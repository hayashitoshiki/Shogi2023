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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.entity.game.rule.PieceSetUpRule
import com.example.home.R
import com.example.home.compoment.HandeSettingBox

@Composable
fun BaseSettingCard(
    modifier: Modifier = Modifier,
    title: String,
    selected: PieceSetUpRule.Normal,
    onChange: (PieceSetUpRule.Normal) -> Unit,
) {
    Card(modifier = modifier.shadow(8.dp, RoundedCornerShape(8.dp))) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                style = MaterialTheme.typography.headlineMedium,
                text = title,
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                style = MaterialTheme.typography.titleMedium,
                text = stringResource(id = R.string.title_hande),
            )
            HandeSettingBox(
                selected = selected,
                onChange = onChange,
            )
        }
    }
}