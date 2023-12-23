package com.example.service

import com.example.entity.game.board.Board
import com.example.entity.game.board.CellStatus
import com.example.entity.game.board.Position
import com.example.entity.game.board.Stand
import com.example.entity.game.piece.Piece
import com.example.entity.game.rule.PieceSetUpRule
import com.example.entity.game.rule.Turn
import com.example.extention.movePieceByPosition
import com.example.extention.searchMoveBy
import com.example.extention.setUp

/**
 * 将棋に関するドメインロジック
 *
 */
class GameService {

    /**
     * 指定したマスに駒を動かす
     *
     * @param board 将棋盤
     * @param stand 持ち駒
     * @param beforePosition 動かす駒のマス目
     * @param afterPosition 動かす先のマス目
     * @return
     */
    fun movePieceByPosition(
        board: Board,
        stand: Stand,
        beforePosition: Position,
        afterPosition: Position,
    ): Pair<Board, Stand> {
        val afterPositionCellCash = board.getCellByPosition(afterPosition).getStatus()
        board.movePieceByPosition(beforePosition, afterPosition)
        if (afterPositionCellCash is CellStatus.Fill.FromPiece) {
            val holdPiece = when (val piece = afterPositionCellCash.piece) {
                is Piece.Reverse -> piece.degeneracy()
                is Piece.Surface -> piece
            }
            stand.add(holdPiece)
        }
        return Pair(board, stand)
    }

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
