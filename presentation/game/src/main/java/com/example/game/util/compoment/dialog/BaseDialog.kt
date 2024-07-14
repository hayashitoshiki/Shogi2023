package com.example.game.util.compoment.dialog

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun BaseDialog(
    onClickConfirmButton: () -> Unit,
    onClickDismissButton: () -> Unit,
    confirmButtonLabel: String,
    dismissButtonLabel: String,
    title: String,
    description: String,
) {
    AlertDialog(
        onDismissRequest = {
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onClickConfirmButton()
                },
            ) {
                Text(confirmButtonLabel)
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onClickDismissButton()
                },
            ) {
                Text(dismissButtonLabel)
            }
        },
        title = {
            Text(title)
        },
        text = {
            Text(description)
        },
    )
}
