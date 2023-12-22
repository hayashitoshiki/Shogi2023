package com.example.extention

import com.example.entity.game.board.Board
import com.example.entity.game.rule.PieceSetUpRule

/**
 * 将棋盤のセットアップ
 *
 * @param pieceSetUpRule 設定内容
 * @return 設定内容に従った将棋盤の初期値
 */
internal fun Board.Companion.setUp(pieceSetUpRule: PieceSetUpRule): Board {
    return Board(pieceSetUpRule.boardSize).apply {
        pieceSetUpRule.initPiece.forEach { (position, cellStatus) ->
            update(position, cellStatus)
        }
    }
}