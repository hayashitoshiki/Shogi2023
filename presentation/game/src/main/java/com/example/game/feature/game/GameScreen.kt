package com.example.game.feature.game

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.core.component.BaseScreen
import com.example.domainObject.game.rule.Turn
import com.example.game.util.compoment.GameBox
import com.example.game.util.compoment.button.LoseButton
import com.example.game.util.compoment.dialog.EvolutionConfirmDialog
import com.example.game.util.compoment.dialog.GameEndDialog
import com.example.core.navigation.NavigationScreens
import kotlinx.coroutines.flow.Flow

object GameScreen: BaseScreen<GameViewModel, GameViewModel.UiState, GameViewModel.Effect, GameViewModel.Action>() {
    @Composable
    override fun Effect(
        navController: NavHostController,
        effect: Flow<GameViewModel.Effect>,
        uiState: State<GameViewModel.UiState>,
        action: (GameViewModel.Action) -> Unit,
    ) {
        val showEvolutionDialog = remember { mutableStateOf<GameViewModel.Effect.ShowEvolutionDialog?>(null) }
        val showGameEndDialog = remember { mutableStateOf<GameViewModel.Effect.ShowGameEndDialog?>(null) }

        LaunchedEffect(true) {
            effect.collect { effect ->
                when (effect) {
                    is GameViewModel.Effect.ShowEvolutionDialog -> {
                        showEvolutionDialog.value = effect
                    }
                    is GameViewModel.Effect.ShowGameEndDialog -> {
                        showGameEndDialog.value = effect
                    }
                    GameViewModel.Effect.NavigateHomeScreen -> navController.popBackStack()
                    GameViewModel.Effect.NavigateReplayScreen -> {
                        navController.navigate(NavigationScreens.REPLAY_SCREEN.route) {
                            popUpTo(NavigationScreens.REPLAY_SCREEN.route) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            }
        }

        showGameEndDialog.value?.apply {
            GameEndDialog(
                openDialog = showGameEndDialog,
                onClickNavigationHome = { action(GameViewModel.Action.ClickGameEndDialogHomeButton) },
                onClickNavigationReplay = { action(GameViewModel.Action.ClickGameEndDialogReplayButton) },
            )
        }
        showEvolutionDialog.value?.apply {
            EvolutionConfirmDialog(
                openDialog = showEvolutionDialog,
                onClick = { action(GameViewModel.Action.ClickEvolutionConfirmDialogConfirmButton(position, it)) },
            )
        }
    }

    @Composable
    override fun View(
        modifier: Modifier,
        uiState: State<GameViewModel.UiState>,
        action: (GameViewModel.Action) -> Unit
    ) {
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
                    onClick = { action(GameViewModel.Action.ClickLoseButton(it)) },
                )
                GameBox(
                    whiteStand = uiState.value.whiteStand,
                    blackStand = uiState.value.blackStand,
                    blackTimeLimit = uiState.value.blackTimeLimit,
                    whiteTimeLimit = uiState.value.whiteTimeLimit,
                    onStandClick = { piece, turn -> action(GameViewModel.Action.TapStand(piece, turn)) },
                    onBoardClick = { action(GameViewModel.Action.TapBoard(it)) },
                    board = uiState.value.board,
                    hintList = uiState.value.readyMoveInfo?.hintList ?: emptyList(),
                )
                LoseButton(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    turn = Turn.Normal.Black,
                    enable = uiState.value.turn == Turn.Normal.Black,
                    onClick = { action(GameViewModel.Action.ClickLoseButton(it)) },
                )
            }
        }
    }
}
