package com.example.game.util.compoment.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.game.R
import com.example.game.feature.game.GameViewModel
import com.example.test.theme.Shogi2023Theme

@Composable
fun EvolutionConfirmDialog(
    openDialog: MutableState<GameViewModel.Effect.Evolution?>,
    onClick: (Boolean) -> Unit,
) {
    val context = LocalContext.current
    BaseDialog(
        onClickConfirmButton = {
            openDialog.value = null
            onClick(true)
        },
        onClickDismissButton = {
            openDialog.value = null
            onClick(false)
        },
        confirmButtonLabel = context.getString(R.string.dialog_text_yes),
        dismissButtonLabel = context.getString(R.string.dialog_text_no),
        title = context.getString(R.string.dialog_evolution_title),
        description = context.getString(R.string.dialog_evolution_body),
    )
}

@Preview(showBackground = true)
@Composable
internal fun EvolutionConfirmDialogPreview() {
    val showEvolutionDialog = remember { mutableStateOf<GameViewModel.Effect.Evolution?>(null) }

    Shogi2023Theme {
        EvolutionConfirmDialog(
            openDialog = showEvolutionDialog,
            onClick = {},
        )
    }
}
