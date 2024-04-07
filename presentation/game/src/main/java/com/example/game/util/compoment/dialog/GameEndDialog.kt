package com.example.game.util.compoment.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import com.example.game.R
import com.example.game.feature.game.GameViewModel
import com.example.game.util.extension.stringRes

@Composable
fun GameEndDialog(
    openDialog: MutableState<GameViewModel.Effect.GameEnd?>,
    onClickNavigationHome: () -> Unit,
    onClickNavigationReplay: () -> Unit,
    ) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = {
        },
        confirmButton = {
            TextButton(
                onClick = {
                    openDialog.value = null
                    onClickNavigationHome()
                }
            ) {
                Text(context.getString(R.string.dialog_game_end_confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    openDialog.value = null
                    onClickNavigationReplay()
                }
            ) {
                Text(context.getString(R.string.dialog_game_end_dismiss))
            }
        },
        title = {
            Text(context.getString(R.string.dialog_game_end_title))
        },
        text = {
            val text = when(val gameEnd = openDialog.value) {
                GameViewModel.Effect.GameEnd.Draw -> {
                    context.getString(R.string.dialog_game_end_body_draw)
                }
                is GameViewModel.Effect.GameEnd.Win -> {
                    val turnText = context.getString(gameEnd.turn.stringRes)
                    context.getString(R.string.dialog_game_end_body_win, turnText)
                }
                null -> ""
            }
            Text(text)

        },
    )
}