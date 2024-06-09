package com.example.domainObject.game.rule

/**
 * プレイヤーのルール
 *
 * @property blackRule 先手のルール
 * @property whiteRule 後手のルール
 */
data class PlayersRule(
    val blackRule: PlayerRule,
    val whiteRule: PlayerRule,
) {
    companion object {
        val INIT = PlayersRule(
            blackRule = PlayerRule(),
            whiteRule = PlayerRule(),
        )
    }
}
