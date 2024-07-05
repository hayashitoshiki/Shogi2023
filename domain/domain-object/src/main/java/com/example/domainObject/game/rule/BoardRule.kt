package com.example.domainObject.game.rule

import com.example.domainObject.game.board.Size

/**
 * 将棋盤の設定
 *
 * @property boardSize 将棋盤のサイズ
 * @property setUpRule 駒のルール
 * @property boardHandeRule 駒落ちルール
 */
data class BoardRule(
    val setUpRule: SetUpRule = SetUpRule.NORMAL,
    val boardHandeRule: BoardHandeRule = BoardHandeRule.DEFAULT,
) {
    val boardSize: Size = Size(9, 9)

    /**
     * 駒の初期表示ルール
     *
     */
    enum class SetUpRule {
        NORMAL,
    }

    companion object {
        val DEFAULT = BoardRule(
            setUpRule = SetUpRule.NORMAL,
            boardHandeRule = BoardHandeRule.DEFAULT
        )
    }
}
