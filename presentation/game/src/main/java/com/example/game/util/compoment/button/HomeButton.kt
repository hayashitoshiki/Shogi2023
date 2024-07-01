package com.example.game.util.compoment.button

import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.game.R

@Composable
fun HomeButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    val showDialog = remember { mutableStateOf(false) }

    IconButton(
        modifier = modifier.size(48.dp),
        onClick = { showDialog.value = true },
    ) {
        Icon(
            modifier = modifier.size(32.dp),
            painter = painterResource(R.drawable.ic_home),
            contentDescription = "",
        )
    }
    if (showDialog.value) {
        ConfirmReplayEndDialog(openDialog = showDialog, onClick = onClick)
    }
}

@Composable
fun ConfirmReplayEndDialog(
    openDialog: MutableState<Boolean>,
    onClick: () -> Unit,
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
                },
            ) {
                Text(context.getString(R.string.dialog_text_yes))
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    openDialog.value = false
                },
            ) {
                Text(context.getString(R.string.dialog_text_no))
            }
        },
        title = {
            Text(context.getString(R.string.dialog_confirm_replay_end_title))
        },
        text = {
            Text(context.getString(R.string.dialog_confirm_replay_end_body))
        },
    )
}
