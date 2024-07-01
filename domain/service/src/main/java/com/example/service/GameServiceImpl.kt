package com.example.service

import com.example.domainLogic.board.isAvailableKingBy
import com.example.domainLogic.board.isCheckByTurn
import com.example.domainLogic.board.movePieceByPosition
import com.example.domainLogic.board.searchMoveBy
import com.example.domainLogic.board.searchPutBy
import com.example.domainLogic.piece.degeneracy
import com.example.domainLogic.rule.changeNextTurn
import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Cell
import com.example.domainObject.game.board.CellStatus
import com.example.domainObject.game.board.Position
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.piece.Piece
import com.example.domainObject.game.rule.GameRule
import com.example.domainObject.game.rule.Turn
import com.example.serviceinterface.GameService
import javax.inject.Inject

class GameServiceImpl @Inject constructor() : GameService {

    override fun isCheckmate(board: Board, stand: Stand, turn: Turn): Boolean {
        board.getCellsFromTurn(turn).forEach { position ->
            board.searchMoveBy(position, turn).forEach { afterPosition ->
                val newBoard = movePieceByPosition(board, stand, position, afterPosition).first
                if (!newBoard.isCheckByTurn(turn)) return false
            }
        }

        stand.pieces.forEach { piece ->
            searchPutBy(piece, board, turn).forEach { position ->
                val newBoard = putPieceByStand(board, stand, turn, piece, position).first
                if (!newBoard.isCheckByTurn(turn)) return false
            }
        }

        return true
    }

    override fun searchPutBy(piece: Piece, board: Board, turn: Turn): List<Position> {
        return board.searchPutBy(piece, turn)
            .filter { !checkPutFuCheckMate(piece, board, turn) }
    }

    /**
     * 打ち歩詰めか判定
     *
     * @param piece 打った駒
     * @param board 将棋盤
     * @param turn 手番
     * @return 打ち歩詰め判定結果
     */
    private fun checkPutFuCheckMate(piece: Piece, board: Board, turn: Turn): Boolean {
        return if (piece is Piece.Surface.Fu) {
            isCheckmate(board, Stand(), turn)
        } else {
            false
        }
    }

    override fun putPieceByStand(
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

    override fun movePieceByPosition(
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

    override fun checkGameSet(
        board: Board,
        stand: Stand,
        turn: Turn,
        rule: GameRule,
    ): Boolean {
        val nextTurn = turn.changeNextTurn()
        return !board.isAvailableKingBy(nextTurn) ||
            isCheckmate(board, stand, nextTurn) ||
            checkGameSetForFirstCheck(board, turn, rule) ||
            checkTryGameSet(board, turn, rule)
    }

    override fun checkDraw(boardLog: Map<Map<Position, Cell>, Int>, board: Board): Boolean {
        val alreadyBoardCount = boardLog[board.getAllCells()] ?: 0
        return alreadyBoardCount == 3
    }

    /**
     * 王手将棋判定
     *
     * @param board 将棋盤
     * @param turn 手番
     * @param rule ルール
     * @return 王手将棋判定結果
     */
    private fun checkGameSetForFirstCheck(board: Board, turn: Turn, rule: GameRule): Boolean {
        val nextTurn = turn.changeNextTurn()
        return when {
            turn is Turn.Normal.Black && rule.playersRule.blackRule.isFirstCheckEnd -> {
                board.isCheckByTurn(nextTurn)
            }

            turn is Turn.Normal.White && rule.playersRule.whiteRule.isFirstCheckEnd -> {
                board.isCheckByTurn(nextTurn)
            }

            else -> false
        }
    }

    /**
     * トライルール判定
     *
     * @param board 将棋盤
     * @param turn 手番
     * @param rule ルール
     * @return トライルール判定結果
     */
    private fun checkTryGameSet(board: Board, turn: Turn, rule: GameRule): Boolean {
        return when (turn) {
            Turn.Normal.Black -> {
                if (!rule.playersRule.blackRule.isTryRule) return false
                (board.getCellByPosition(Position(5, 1)).getStatus() as? CellStatus.Fill.FromPiece)
                    ?.piece == Piece.Surface.Gyoku
            }

            Turn.Normal.White -> {
                if (!rule.playersRule.whiteRule.isTryRule) return false
                (board.getCellByPosition(Position(5, 9)).getStatus() as? CellStatus.Fill.FromPiece)
                    ?.piece == Piece.Surface.Ou
            }
        }
    }
}
