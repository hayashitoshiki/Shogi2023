package com.example.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.entity.game.rule.Turn
import com.example.game.compoment.BoardBox
import com.example.game.compoment.StandBox
import com.example.game.compoment.button.LoseButton
import com.example.game.compoment.dialog.EvolutionDialog
import com.example.game.compoment.dialog.GameEndDialog

@Composable
fun GameScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: GameViewModel,
) {
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
            onClick = { navController.popBackStack() },
        )
    }

    val backHandlingEnabled by remember { mutableStateOf(true) }
    BackHandler(backHandlingEnabled) { }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column {
            LoseButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                turn = Turn.Normal.White,
                onClick = viewModel::tapLoseButton,
            )
            StandBox(
                stand = uiState.value.whiteStand,
                turn = Turn.Normal.White,
                onClick = viewModel::tapStand,
            )
            BoardBox(
                board = uiState.value.board,
                onClick = viewModel::tapBoard,
                hintList = uiState.value.readyMoveInfo?.hintList ?: listOf(),
            )
            StandBox(
                stand = uiState.value.blackStand,
                turn = Turn.Normal.Black,
                onClick = viewModel::tapStand,
            )
            LoseButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                turn = Turn.Normal.Black,
                onClick = viewModel::tapLoseButton,
            )
        }
    }
}
