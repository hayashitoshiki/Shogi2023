package com.example.home.game.compoment.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.LocalContext
import com.example.home.R
import com.example.home.game.GameViewModel

@Composable
fun EvolutionDialog(openDialog: MutableState<GameViewModel.Effect.Evolution?>, onClick: () -> Unit) {
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
                Text(context.getString(R.string.dialog_text_yes))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    openDialog.value = null
                }
            ) {
                Text(context.getString(R.string.dialog_text_no))
            }
        },
        title = {
            Text(context.getString(R.string.dialog_evolution_title))
        },
        text = {
            Text(context.getString(R.string.dialog_evolution_body))
        },
    )
}