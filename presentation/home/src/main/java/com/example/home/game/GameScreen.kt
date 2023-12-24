package com.example.home.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.home.game.compoment.BoardBox

@Composable
fun GameScreen(modifier: Modifier = Modifier, viewModel: GameViewModel) {
    val uiState = viewModel.uiState.collectAsState()

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        BoardBox(modifier = Modifier, board = uiState.value.board, onClick = {})
    }
}
