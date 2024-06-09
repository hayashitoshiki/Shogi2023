package com.example.service

import com.example.domainObject.game.Log
import com.example.domainObject.game.MoveTarget
import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.CellStatus
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.piece.Piece
import com.example.domainLogic.piece.degeneracy
import com.example.domainLogic.piece.evolution
import com.example.domainLogic.rule.getOpponentTurn
import com.example.serviceinterface.ReplayService
import javax.inject.Inject

class ReplayServiceImpl @Inject constructor() : ReplayService {

    override fun goNext(board: Board, stand: Stand, log: Log): Pair<Board, Stand> {
        when (val moveTarget = log.moveTarget) {
            is MoveTarget.Board -> {
                board.getPieceOrNullByPosition(moveTarget.position)?.let { cellStatus ->
                    val piece = cellStatus.piece
                    val movePiece = if (log.isEvolution && piece is Piece.Surface) {
                        piece.evolution() ?: piece
                    } else {
                        piece
                    }

                    board.update(moveTarget.position, CellStatus.Empty)
                    board.update(log.afterPosition, CellStatus.Fill.FromPiece(movePiece, log.turn))
                }
            }

            is MoveTarget.Stand -> {
                stand.remove(moveTarget.piece)
                board.update(
                    log.afterPosition,
                    CellStatus.Fill.FromPiece(moveTarget.piece, log.turn)
                )
            }
        }
        standPieceUpdate(stand, log.takePiece, true)

        return board to stand
    }

    override fun goBack(board: Board, stand: Stand, log: Log): Pair<Board, Stand> {
        when (val moveTarget = log.moveTarget) {
            is MoveTarget.Board -> {
                board.getPieceOrNullByPosition(log.afterPosition)?.let { movedCellStatus ->
                    val piece = movedCellStatus.piece
                    val turn = log.turn
                    val movePiece = if (log.isEvolution && piece is Piece.Reverse) {
                        piece.degeneracy()
                    } else {
                        piece
                    }
                    val takedPiece = log.takePiece?.let {
                        val opponentTurn = turn.getOpponentTurn()
                        CellStatus.Fill.FromPiece(it, opponentTurn)
                    } ?: CellStatus.Empty

                    board.update(log.afterPosition, takedPiece)
                    board.update(moveTarget.position, CellStatus.Fill.FromPiece(movePiece, turn))
                }
            }

            is MoveTarget.Stand -> {
                stand.add(moveTarget.piece)
                board.update(log.afterPosition, CellStatus.Empty)
            }
        }
        standPieceUpdate(stand, log.takePiece, false)

        return board to stand
    }

    private fun standPieceUpdate(stand: Stand, takePiece: Piece?, isAdd: Boolean) {
        takePiece?.also { takePiece ->
            val stanePiece = when (takePiece) {
                is Piece.Reverse -> takePiece.degeneracy()
                is Piece.Surface -> takePiece
            }
            if (isAdd) {
                stand.add(stanePiece)
            } else {
                stand.remove(stanePiece)
            }
        }
    }
}
