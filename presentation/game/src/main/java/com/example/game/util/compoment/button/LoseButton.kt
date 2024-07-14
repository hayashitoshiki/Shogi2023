package com.example.game.util.compoment.button

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.domainObject.game.rule.Turn
import com.example.game.R
import com.example.game.util.compoment.dialog.LoseButtonConfirmDialog
import com.example.test.theme.Shogi2023Theme

@Composable
fun LoseButton(modifier: Modifier = Modifier, enable: Boolean, turn: Turn, onClick: (Turn) -> Unit) {
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }
    val rotate = when (turn) {
        Turn.Normal.Black -> 0f
        Turn.Normal.White -> 180f
    }

    Button(
        modifier = modifier.rotate(rotate),
        enabled = enable,
        onClick = { showDialog.value = true },
    ) {
        Text(text = context.getString(R.string.button_lose_text))
    }
    if (showDialog.value) {
        LoseButtonConfirmDialog(
            showDialog = showDialog,
            onClickConfirmButton = { onClick(turn) },
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun HLoseButtonPreview(
    @PreviewParameter(LoseButtonPreviewParameterProvider::class) uiModel: LoseButtonUiModel,
) {
    Shogi2023Theme {
        LoseButton(
            enable = uiModel.enable,
            turn = uiModel.turn,
            onClick = {},
        )
    }
}

internal class LoseButtonPreviewParameterProvider : PreviewParameterProvider<LoseButtonUiModel> {
    override val values: Sequence<LoseButtonUiModel> = sequenceOf(
        LoseButtonUiModel(
            enable = false,
            turn = Turn.Normal.Black,
        ),
        LoseButtonUiModel(
            enable = true,
            turn = Turn.Normal.Black,
        ),
        LoseButtonUiModel(
            enable = false,
            turn = Turn.Normal.White,
        ),
        LoseButtonUiModel(
            enable = true,
            turn = Turn.Normal.White,
        ),
    )
}

internal data class LoseButtonUiModel(
    val enable: Boolean,
    val turn: Turn,
)
