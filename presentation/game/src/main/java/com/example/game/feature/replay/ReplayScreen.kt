package com.example.game.feature.replay

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.core.navigation.NavigationScreens
import com.example.game.util.compoment.GameBox
import com.example.game.util.compoment.button.HomeButton
import com.example.game.util.compoment.button.ReStartButton

@Composable
fun ReplayScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: ReplayViewModel,
) {
    val uiState = viewModel.uiState.collectAsState()
    val backHandlingEnabled by remember { mutableStateOf(true) }
    BackHandler(backHandlingEnabled) { }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .align(Alignment.TopEnd),
        ) {
            HomeButton(
                onClick = {
                    navController.navigate(NavigationScreens.HOMET_SCREEN.route) {
                        popUpTo(NavigationScreens.HOMET_SCREEN.route) {
                            inclusive = true
                        }
                    }
                }
            )
            ReStartButton(
                onClick = {
                    navController.navigate(NavigationScreens.GAME_SCREEN.route) {
                        popUpTo(NavigationScreens.GAME_SCREEN.route) {
                            inclusive = true
                        }
                    }
                }
            )
        }
        Box(
            modifier = modifier.wrapContentSize(),
            contentAlignment = Alignment.Center,
        ) {
            GameBox(
                whiteStand = uiState.value.whiteStand,
                blackStand = uiState.value.blackStand,
                blackTimeLimit = uiState.value.blackTimeLimit,
                whiteTimeLimit = uiState.value.whiteTimeLimit,
                onStandClick = { _, _ -> },
                onBoardClick = {},
                board = uiState.value.board,
                hintList = emptyList(),
            )
            Row(modifier = Modifier.matchParentSize()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { viewModel.tagLeft() }
                )
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .clickable { viewModel.tagRight() }
                )
            }
        }
    }
}
