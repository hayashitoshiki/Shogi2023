package com.example.home.game

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.entity.game.rule.Turn
import com.example.home.game.compoment.BoardBox
import com.example.home.game.compoment.StandBox
import com.example.home.game.compoment.dialog.EvolutionDialog
import com.example.home.game.compoment.dialog.GameEndDialog

@Composable
fun GameScreen(modifier: Modifier = Modifier, viewModel: GameViewModel) {
    val uiState = viewModel.uiState.collectAsState()
    val showEvolutionDialog = remember { mutableStateOf<GameViewModel.Effect.Evolution?>(null) }
    val showGameEndDialog = remember { mutableStateOf<GameViewModel.Effect.GameEnd?>(null) }
    LaunchedEffect(true) {
        viewModel.evolutionEffect.collect { showEvolutionDialog.value = it }
    }
    LaunchedEffect(true) {
        viewModel.gameEndEffect.collect { showGameEndDialog.value = it }
    }
    showEvolutionDialog.value?.apply {
        EvolutionDialog(
            openDialog = showEvolutionDialog,
            onClick = { viewModel.setEvolution(position) },
        )
    }
    showGameEndDialog.value?.apply {
        GameEndDialog(
            openDialog = showGameEndDialog,
            onClick = { },
        )
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column {
            StandBox(
                modifier = Modifier,
                stand = uiState.value.whiteStand,
                turn = Turn.Normal.White,
                onClick = viewModel::tapStand,
            )
            BoardBox(
                modifier = Modifier,
                board = uiState.value.board,
                onClick = viewModel::tapBoard,
                hintList = uiState.value.readyMoveInfo?.hintList ?: listOf(),
            )
            StandBox(
                modifier = Modifier,
                stand = uiState.value.blackStand,
                turn = Turn.Normal.Black,
                onClick = viewModel::tapStand,
            )
        }
    }
}
