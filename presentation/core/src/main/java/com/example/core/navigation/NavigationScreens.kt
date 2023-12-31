package com.example.core.navigation

/**
 * 画面定義
 *
 * @property route 遷移パス
 */
enum class NavigationScreens(
    val route: String,
) {
    HOMET_SCREEN("home_screen_navigate"),
    GAME_SCREEN("game_screen_navigate"),
    REPLAY_SCREEN("replay_screen_navigate");
}