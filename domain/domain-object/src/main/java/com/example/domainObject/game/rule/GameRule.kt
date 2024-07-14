package com.example.domainObject.game.rule

/**
 * 対局ルール
 *
 * @property boardRule 将棋盤や駒に関するのルール
 * @property logicRule 将棋のロジックに関するルール
 * @property timeLimitRule 持ち時間に関するルール
 */
data class GameRule(
    val boardRule: BoardRule,
    val logicRule: GameLogicRule,
    val timeLimitRule: GameTimeLimitRule,
) {
    companion object {
        val DEFAULT = GameRule(
            boardRule = BoardRule.DEFAULT,
            logicRule = GameLogicRule.DEFAULT,
            timeLimitRule = GameTimeLimitRule.INIT,
        )
    }
}
