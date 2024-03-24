package com.example.service

import com.example.entity.game.board.Board
import com.example.entity.game.board.CellStatus
import com.example.entity.game.board.Position
import com.example.entity.game.board.Stand
import com.example.entity.game.piece.Piece
import com.example.entity.game.rule.GameRule
import com.example.entity.game.rule.Turn
import com.example.extention.changeNextTurn
import com.example.extention.degeneracy
import com.example.extention.isAvailableKingBy
import com.example.extention.isCheckByTurn
import com.example.extention.movePieceByPosition
import com.example.extention.searchMoveBy
import com.example.extention.searchPutBy

/**
 * 将棋に関するドメインロジック
 *
 */
class GameService {

    /**
     * 指定した手番の王様が詰んでいるか判定
     *
     * @param board 将棋盤
     * @param stand 持ち駒
     * @param turn 手番
     * @return 詰んでいるか
     */
    fun isCheckmate(board: Board, stand: Stand, turn: Turn): Boolean {
        board.getCellsFromTurn(turn).forEach { position ->
            board.searchMoveBy(position, turn).forEach { afterPosition ->
                val newBoard = movePieceByPosition(board, stand, position, afterPosition).first
                if (!newBoard.isCheckByTurn(turn)) return false
            }
        }

        stand.pieces.forEach { piece ->
            board.searchPutBy(piece, turn).forEach { position ->
                val newBoard = putPieceByStand(board, stand, turn, piece, position).first
                if (!newBoard.isCheckByTurn(turn)) return false
            }
        }

        return true
    }

    /**
     * 持ち駒の駒を打つ
     *
     * @param board 将棋盤
     * @param stand 持ち駒
     * @param turn 手番
     * @param piece 打つ駒
     * @param position 打つ場所
     */
    fun putPieceByStand(
        board: Board,
        stand: Stand,
        turn: Turn,
        piece: Piece,
        position: Position,
    ): Pair<Board, Stand> {
        val newBoard = board.copy()
        val newStand = stand.copy()
        newBoard.update(position, CellStatus.Fill.FromPiece(piece, turn))
        newStand.remove(piece)
        return Pair(newBoard, newStand)
    }

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
        val newBoard = board.copy()
        val newStand = stand.copy()
        newBoard.movePieceByPosition(beforePosition, afterPosition)
        board.getPieceOrNullByPosition(afterPosition)?.let { afterPositionCellCash ->
            val holdPiece = when (val piece = afterPositionCellCash.piece) {
                is Piece.Reverse -> piece.degeneracy()
                is Piece.Surface -> piece
            }
            newStand.add(holdPiece)
        }

        return Pair(newBoard, newStand)
    }

    /**
     * 勝利判定
     *
     * @param board 将棋盤
     * @param stand 持ち駒
     * @param turn 手番
     * @return 判定結果
     */
    fun checkGameSet(
        board: Board,
        stand: Stand,
        turn: Turn,
    ): Boolean {
        val nextTurn = turn.changeNextTurn()
        return !board.isAvailableKingBy(nextTurn) || isCheckmate(board, stand, nextTurn)
    }

    /**
     * 王手将棋かつ詰み判定
     *
     * @param board 将棋盤
     * @param turn 手番
     * @param rule ルール
     * @return 王手将棋でかつ詰んでいるか
     */
    fun checkGameSetForFirstCheck(board: Board, turn: Turn, rule: GameRule): Boolean {
        val nextTurn = turn.changeNextTurn()
        return when {
            turn is Turn.Normal.Black && rule.usersRule.blackRule.isFirstCheckEnd -> {
                board.isCheckByTurn(nextTurn)
            }

            turn is Turn.Normal.White && rule.usersRule.whiteRule.isFirstCheckEnd -> {
                board.isCheckByTurn(nextTurn)
            }

            else -> false
        }
    }
}
