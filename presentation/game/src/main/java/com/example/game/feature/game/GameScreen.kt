package com.example.game.feature.game

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
import com.example.core.navigation.NavigationScreens
import com.example.entity.game.rule.Turn
import com.example.game.util.compoment.GameBox
import com.example.game.util.compoment.button.LoseButton
import com.example.game.util.compoment.dialog.EvolutionDialog
import com.example.game.util.compoment.dialog.GameEndDialog

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
            onClick = { viewModel.setEvolution(position, it) },
        )
    }
    showGameEndDialog.value?.apply {
        GameEndDialog(
            openDialog = showGameEndDialog,
            onClickNavigationHome = { navController.popBackStack() },
            onClickNavigationReplay = {
                navController.navigate(NavigationScreens.REPLAY_SCREEN.route) {
                    popUpTo(NavigationScreens.REPLAY_SCREEN.route) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }

            },
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
                enable = uiState.value.turn == Turn.Normal.White,
                onClick = viewModel::tapLoseButton,
            )
            GameBox(
                whiteStand = uiState.value.whiteStand,
                blackStand = uiState.value.blackStand,
                onStandClick = viewModel::tapStand,
                onBoardClick = viewModel::tapBoard,
                board = uiState.value.board,
                hintList = uiState.value.readyMoveInfo?.hintList ?: emptyList(),
            )
            LoseButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                turn = Turn.Normal.Black,
                enable = uiState.value.turn == Turn.Normal.Black,
                onClick = viewModel::tapLoseButton,
            )
        }
    }
}
