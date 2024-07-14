package com.example.game.util.compoment.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.game.R
import com.example.test.theme.Shogi2023Theme

@Composable
fun HomeButtonConfirmDialog(
    showDialog: MutableState<Boolean>,
    onClickConfirmButton: () -> Unit,
) {
    val context = LocalContext.current
    BaseDialog(
        onClickConfirmButton = {
            showDialog.value = false
            onClickConfirmButton()
        },
        onClickDismissButton = {
            showDialog.value = false
        },
        confirmButtonLabel = context.getString(R.string.dialog_text_yes),
        dismissButtonLabel = context.getString(R.string.dialog_text_no),
        title = context.getString(R.string.dialog_confirm_replay_end_title),
        description = context.getString(R.string.dialog_confirm_replay_end_body),
    )
}

@Preview(showBackground = true)
@Composable
internal fun HomeButtonConfirmDialogPreview() {
    val showDialog = remember { mutableStateOf(false) }
    Shogi2023Theme {
        HomeButtonConfirmDialog(
            showDialog = showDialog,
            onClickConfirmButton = {},
        )
    }
}
