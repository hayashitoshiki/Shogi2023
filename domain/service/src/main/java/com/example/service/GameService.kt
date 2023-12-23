package com.example.service

import com.example.entity.game.board.Board
import com.example.entity.game.board.Position
import com.example.entity.game.rule.PieceSetUpRule
import com.example.entity.game.rule.Turn
import com.example.extention.searchMoveBy
import com.example.extention.setUp

/**
 * 将棋に関するドメインロジック
 *
 */
class GameService {

    /**
     * 指定したマスの駒の動かせる場所を検索
     *
     * @param board 将棋盤
     * @param position 指定するマス
     * @param turn 手番
     * @return 動かせるマスのリスト
     */
    fun searchMoveBy(board: Board, position: Position, turn: Turn): List<Position> {
        return board.searchMoveBy(position, turn)
    }

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
