package com.example.domainObject.game.rule

/**
 * 対局ルール
 *
 * @property boardRule 将棋盤のルール
 * @property playersRule プレイヤーのルール
 */
data class GameRule(
    val boardRule: BoardRule,
    val playersRule: PlayersRule,
) {
    companion object
}
