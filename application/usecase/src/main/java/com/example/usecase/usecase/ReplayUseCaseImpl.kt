package com.example.usecase.usecase

import com.example.entity.game.Log
import com.example.entity.game.MoveTarget
import com.example.entity.game.board.Board
import com.example.entity.game.board.CellStatus
import com.example.entity.game.board.Stand
import com.example.entity.game.piece.Piece
import com.example.entity.game.rule.PieceSetUpRule
import com.example.entity.game.rule.Turn
import com.example.repository.repositoryinterface.GameRuleRepository
import com.example.repository.repositoryinterface.LogRepository
import com.example.service.GameService
import com.example.usecase.usecaseinterface.ReplayUseCase
import com.example.usecase.usecaseinterface.model.result.ReplayGoBackResult
import com.example.usecase.usecaseinterface.model.result.ReplayGoNextResult
import com.example.usecase.usecaseinterface.model.result.ReplayInitResult
import javax.inject.Inject

class ReplayUseCaseImpl @Inject constructor(
    private val logRepository: LogRepository,
    private val gameRuleRepository: GameRuleRepository,
) : ReplayUseCase {
    private val gameService = GameService()

    override fun replayInit(pieceSetUpRule: PieceSetUpRule): ReplayInitResult {
        val rule = gameRuleRepository.getGameRule()
        val board = gameService.setUpBoard(rule)
        val blackStand = Stand()
        val whiteStand = Stand()
        val log = logRepository.getLatestLog()
        return ReplayInitResult(
            board = board,
            blackStand = blackStand,
            whiteStand = whiteStand,
            log = log,
        )
    }

    override fun goNext(board: Board, stand: Stand, log: Log): ReplayGoNextResult {
        when (val moveTarget = log.moveTarget) {
            is MoveTarget.Board -> {
                val cellStatus = board.getCellByPosition(moveTarget.position).getStatus()
                if (cellStatus is CellStatus.Fill.FromPiece) {
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

        return ReplayGoNextResult(
            board = board,
            stand = stand,
        )
    }

    override fun goBack(board: Board, stand: Stand, log: Log): ReplayGoBackResult {
        when (val moveTarget = log.moveTarget) {
            is MoveTarget.Board -> {
                val movedCellStatus = board.getCellByPosition(log.afterPosition).getStatus()
                if (movedCellStatus is CellStatus.Fill.FromPiece) {
                    val piece = movedCellStatus.piece
                    val movePiece = if (log.isEvolution && piece is Piece.Reverse) {
                        piece.degeneracy()
                    } else {
                        piece
                    }
                    val takedPiece = log.takePiece?.let {
                        val opponentTurn = when (log.turn) {
                            Turn.Normal.Black -> Turn.Normal.White
                            Turn.Normal.White -> Turn.Normal.Black
                        }
                        CellStatus.Fill.FromPiece(it, opponentTurn)
                    } ?: CellStatus.Empty

                    board.update(log.afterPosition, takedPiece)
                    board.update(
                        moveTarget.position,
                        CellStatus.Fill.FromPiece(movePiece, log.turn)
                    )
                }
            }

            is MoveTarget.Stand -> {
                stand.add(moveTarget.piece)
                board.update(log.afterPosition, CellStatus.Empty)
            }
        }
        standPieceUpdate(stand, log.takePiece, false)

        return ReplayGoBackResult(
            board = board,
            stand = stand,
        )
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
