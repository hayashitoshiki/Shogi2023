package com.example.game.compoment.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import com.example.game.R
import com.example.game.GameViewModel
import com.example.game.extension.stringRes

@Composable
fun GameEndDialog(openDialog: MutableState<GameViewModel.Effect.GameEnd?>, onClick: () -> Unit) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = {
        },
        confirmButton = {
            TextButton(
                onClick = {
                    openDialog.value = null
                    onClick()
                }
            ) {
                Text(context.getString(R.string.dialog_game_end_confirm))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    openDialog.value = null
                }
            ) {
                Text(context.getString(R.string.dialog_game_end_dismiss))
            }
        },
        title = {
            Text(context.getString(R.string.dialog_game_end_title))
        },
        text = {
            val turnText = openDialog.value?.turn?.stringRes?.let {
                context.getString(it)
            }
            Text(context.getString(R.string.dialog_game_end_body, turnText))
        },
    )
}