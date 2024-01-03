package com.example.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.core.navigation.NavigationScreens
import com.example.home.compoment.card.NormalShogiSettingCard

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: HomeViewModel,
) {
    val gameRule = viewModel.uiState.collectAsState()
    val navigateGameScreen = remember { mutableStateOf<HomeViewModel.Effect.GameStart?>(null) }
    LaunchedEffect(true) {
        viewModel.gameStartEffect.collect { navigateGameScreen.value = it }
    }
    navigateGameScreen.value?.apply {
        navController.navigate(NavigationScreens.GAME_SCREEN.route) {
            popUpTo(NavigationScreens.HOMET_SCREEN.route) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Column {
            NormalShogiSettingCard(
                selected = gameRule.value.rule,
                onChange = viewModel::changePieceHande
            )
            Spacer(modifier = Modifier.size(16.dp))
            Button(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp),
                onClick = viewModel::onGameStartClick
            ) {
                Text(text = stringResource(R.string.home_game_start_button))
            }
        }
    }
}
