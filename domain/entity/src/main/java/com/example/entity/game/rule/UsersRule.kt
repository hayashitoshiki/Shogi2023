package com.example.entity.game.rule

/**
 * プレイヤーのルール
 *
 * @property blackRule 先手のルール
 * @property whiteRule 後手のルール
 */
data class UsersRule(
    val blackRule: UserRule,
    val whiteRule: UserRule,
)