package com.example.game.util.compoment.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.game.R
import com.example.game.feature.game.GameViewModel
import com.example.game.util.extension.stringRes
import com.example.test.theme.Shogi2023Theme

@Composable
fun GameEndDialog(
    openDialog: MutableState<GameViewModel.Effect.GameEnd?>,
    onClickNavigationHome: () -> Unit,
    onClickNavigationReplay: () -> Unit,
) {
    val context = LocalContext.current
    val description = when (val gameEnd = openDialog.value) {
        GameViewModel.Effect.GameEnd.Draw -> {
            context.getString(R.string.dialog_game_end_body_draw)
        }
        is GameViewModel.Effect.GameEnd.Win -> {
            val turnText = context.getString(gameEnd.turn.stringRes)
            context.getString(R.string.dialog_game_end_body_win, turnText)
        }
        null -> ""
    }
    BaseDialog(
        onClickConfirmButton = {
            openDialog.value = null
            onClickNavigationHome()
        },
        onClickDismissButton = {
            openDialog.value = null
            onClickNavigationReplay()
        },
        confirmButtonLabel = context.getString(R.string.dialog_game_end_confirm),
        dismissButtonLabel = context.getString(R.string.dialog_game_end_dismiss),
        title = context.getString(R.string.dialog_game_end_title),
        description = description,
    )
}

@Preview(showBackground = true)
@Composable
internal fun GameEndDialogPreview() {
    val showEvolutionDialog = remember { mutableStateOf<GameViewModel.Effect.GameEnd?>(null) }

    Shogi2023Theme {
        GameEndDialog(
            openDialog = showEvolutionDialog,
            onClickNavigationHome = {},
            onClickNavigationReplay = {},
        )
    }
}
