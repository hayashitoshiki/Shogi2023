package com.example.shogi2023

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.core.navigation.NavigationScreens
import com.example.home.game.GameScreen
import com.example.home.home.HomeScreen

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
        startDestination = NavigationScreens.HOMET_SCREEN.route
    ) {
        composable(route = NavigationScreens.HOMET_SCREEN.route) {
            HomeScreen(
                navController = navController,
            )
        }
        composable(route = NavigationScreens.GAME_SCREEN.route) {
            GameScreen(
                navController = navController,
                viewModel = hiltViewModel(),
            )
        }
    }
}
