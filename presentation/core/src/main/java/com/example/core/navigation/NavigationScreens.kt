package com.example.core.navigation

/**
 * 画面定義
 *
 * @property route 遷移パス
 */
enum class NavigationScreens(
    val route: String,
) {
    /**
     * ホーム画面
     */
    HOME_SCREEN("home_screen_navigate"),

    /**
     * ゲーム画面
     */
    GAME_SCREEN("game_screen_navigate"),

    /**
     * リプレイ画面
     */
    REPLAY_SCREEN("replay_screen_navigate"),
}
