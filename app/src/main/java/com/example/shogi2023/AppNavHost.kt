package com.example.shogi2023

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.game.feature.game.GameScreen
import com.example.game.feature.replay.ReplayScreen
import com.example.home.HomeScreen
import com.example.core.navigation.NavigationScreens

/**
 * NavigationHost 画面遷移定義
 *
 * @param navController ナビゲーションAPI
 */
@Composable
fun AppNavHost(
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = NavigationScreens.HOME_SCREEN.route,
    ) {
        composable(route = NavigationScreens.HOME_SCREEN.route) {
            HomeScreen(
                navController = navController,
                viewModel = hiltViewModel(),
            )
        }
        composable(route = NavigationScreens.GAME_SCREEN.route) {
            GameScreen(
                navController = navController,
                viewModel = hiltViewModel(),
            )
        }
        composable(route = NavigationScreens.REPLAY_SCREEN.route) {
            ReplayScreen(
                navController = navController,
                viewModel = hiltViewModel(),
            )
        }
    }
}
