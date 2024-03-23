package com.example.entity.game.rule

/**
 * 対局ルール
 *
 * @property boardRule 将棋盤のルール
 * @property usersRule プレイヤーのルール
 */
data class GameRule(
    val boardRule: BoardRule,
    val usersRule: UsersRule,
)
