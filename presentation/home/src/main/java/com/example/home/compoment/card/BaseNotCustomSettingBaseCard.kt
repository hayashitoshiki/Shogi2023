package com.example.home.compoment.card

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.home.R
import com.example.home.compoment.HandeSettingBox
import com.example.home.model.GameRuleSettingUiModel

@Composable
fun BaseNotCustomSettingBaseCard(
    modifier: Modifier = Modifier,
    title: String,
    selected: GameRuleSettingUiModel.SelectedHande,
    onChange: (GameRuleSettingUiModel.SelectedHande) -> Unit,
) {
    BaseSettingCard(
        modifier = modifier,
        title = title,
    ) {
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
