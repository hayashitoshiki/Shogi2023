package com.example.service

import com.example.domainLogic.piece.degeneracy
import com.example.domainLogic.piece.evolution
import com.example.domainObject.game.log.MoveTarget
import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.CellStatus
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.log.MoveRecode
import com.example.domainObject.game.piece.Piece
import com.example.domainObject.game.rule.Turn
import com.example.domainObject.game.rule.Turn.Normal.Black.getOpponentTurn
import com.example.serviceinterface.ReplayService
import javax.inject.Inject

class ReplayServiceImpl @Inject constructor() : ReplayService {

    override fun goNext(board: Board, blackStand: Stand, whiteStand: Stand, log: MoveRecode): Triple<Board, Stand, Stand> {
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
                when(log.turn) {
                    Turn.Normal.Black -> blackStand.remove(moveTarget.piece)
                    Turn.Normal.White -> whiteStand.remove(moveTarget.piece)
                }

                board.update(
                    log.afterPosition,
                    CellStatus.Fill.FromPiece(moveTarget.piece, log.turn),
                )
            }
        }
        when(log.turn) {
            Turn.Normal.Black -> {
                standPieceUpdate(blackStand, log.takePiece, true)
            }
            Turn.Normal.White -> {
                standPieceUpdate(whiteStand, log.takePiece, true)
            }
        }

        return Triple(board, blackStand, whiteStand)
    }

    override fun goBack(board: Board, blackStand: Stand, whiteStand: Stand, log: MoveRecode): Triple<Board, Stand, Stand> {
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
                when(log.turn) {
                    Turn.Normal.Black -> blackStand.add(moveTarget.piece)
                    Turn.Normal.White -> whiteStand.add(moveTarget.piece)
                }
                board.update(log.afterPosition, CellStatus.Empty)
            }
        }
        when(log.turn) {
            Turn.Normal.Black -> {
                standPieceUpdate(blackStand, log.takePiece, false)
            }
            Turn.Normal.White -> {
                standPieceUpdate(whiteStand, log.takePiece, false)
            }
        }

        return Triple(board, blackStand, whiteStand)
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
