package com.example.service

import com.example.entity.game.board.Board
import com.example.entity.game.rule.PieceSetUpRule
import com.example.extention.setUp

/**
 * 将棋に関するドメインロジック
 *
 */
class GameService {

    /**
     * 将棋盤の初期設定
     *
     * @param pieceSetUpRule 将棋盤の初期設定内容
     * @return 駒を置けるマス一覧
     */
    fun setUpBoard(pieceSetUpRule: PieceSetUpRule): Board {
        return Board.setUp(pieceSetUpRule)
    }
}
