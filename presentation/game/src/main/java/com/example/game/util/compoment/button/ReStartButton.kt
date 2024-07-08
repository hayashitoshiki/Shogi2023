package com.example.game.util.compoment.button

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.game.R
import com.example.game.util.compoment.dialog.RestartButtonConfirmDialog
import com.example.test.theme.Shogi2023Theme

@Composable
fun ReStartButton(modifier: Modifier = Modifier, onClick: () -> Unit) {
    val showDialog = remember { mutableStateOf(false) }
    val context = LocalContext.current

    IconButton(
        modifier = modifier.size(48.dp),
        onClick = { showDialog.value = true },
    ) {
        Icon(
            modifier = modifier.size(32.dp),
            painter = painterResource(R.drawable.ic_restart),
            contentDescription = "",
        )
    }
    if (showDialog.value) {
        RestartButtonConfirmDialog(
            showDialog = showDialog,
            onClickConfirmButton = {
                onClick()
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
internal fun ReStartButtonPreview() {
    Shogi2023Theme {
        ReStartButton(
            onClick = {},
        )
    }
}
