package com.example.domainObject.game.rule

import com.example.domainObject.game.board.Size

/**
 * 将棋盤の設定
 *
 * @property boardSize 将棋盤のサイズ
 * @property setUpRule 駒のルール
 */
data class BoardRule(
    val setUpRule: SetUpRule = SetUpRule.NORMAL,
) {
    val boardSize: Size = Size(9, 9)

    /**
     * 駒の初期表示ルール
     *
     */
    enum class SetUpRule {
        NORMAL,
    }

    companion object
}
