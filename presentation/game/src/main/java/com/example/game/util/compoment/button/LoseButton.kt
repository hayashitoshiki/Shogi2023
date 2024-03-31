package com.example.game.util.compoment.button

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import com.example.entity.game.rule.Turn
import com.example.game.R


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
        onClick = { showDialog.value = true }) {
        Text(text = context.getString(R.string.button_lose_text))
    }
    if (showDialog.value) {
        ConfirmLoseDialog(openDialog = showDialog, onClick = { onClick(turn) })
    }
}


@Composable
fun ConfirmLoseDialog(
    openDialog: MutableState<Boolean>,
    onClick: () -> Unit
) {
    val context = LocalContext.current
    AlertDialog(
        onDismissRequest = {
        },
        confirmButton = {
            TextButton(
                onClick = {
                    openDialog.value = false
                    onClick()
                }
            ) {
                Text(context.getString(R.string.dialog_text_yes))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    openDialog.value = false
                }
            ) {
                Text(context.getString(R.string.dialog_text_no))
            }
        },
        title = {
            Text(context.getString(R.string.dialog_confirm_lose_title))
        },
        text = {
            Text(context.getString(R.string.dialog_confirm_lose_body))
        },
    )
}