package com.example.usecase.usecase

import com.example.entity.game.Log
import com.example.entity.game.MoveTarget
import com.example.entity.game.board.Board
import com.example.entity.game.board.Position
import com.example.entity.game.board.Stand
import com.example.entity.game.piece.Piece
import com.example.entity.game.rule.Turn
import com.example.extention.changeNextTurn
import com.example.extention.checkPieceEvolution
import com.example.extention.getOpponentTurn
import com.example.extention.isCheckByTurn
import com.example.extention.isKingCellBy
import com.example.extention.searchMoveBy
import com.example.extention.searchPutBy
import com.example.extention.setUp
import com.example.extention.shouldEvolution
import com.example.extention.updatePieceEvolution
import com.example.repository.repositoryinterface.GameRuleRepository
import com.example.repository.repositoryinterface.LogRepository
import com.example.service.GameService
import com.example.usecase.usecaseinterface.GameUseCase
import com.example.usecase.usecaseinterface.model.ReadyMoveInfoUseCaseModel
import com.example.usecase.usecaseinterface.model.result.GameInitResult
import com.example.usecase.usecaseinterface.model.result.NextResult
import java.time.LocalDateTime
import javax.inject.Inject

class GameUseCaseImpl @Inject constructor(
    private val logRepository: LogRepository,
    private val gameRuleRepository: GameRuleRepository,
) : GameUseCase {
    private val gameService = GameService()

    override fun gameInit(): GameInitResult {
        val rule = gameRuleRepository.getGameRule()
        val board = Board.setUp(rule)
        val blackStand = Stand()
        val whiteStand = Stand()
        val now = LocalDateTime.now()
        logRepository.createGameLog(now)

        return GameInitResult(
            board = board,
            blackStand = blackStand,
            whiteStand = whiteStand,
            turn = Turn.Normal.Black,
        )
    }

    override fun next(
        board: Board,
        stand: Stand,
        turn: Turn,
        touchAction: MoveTarget,
        holdMove: ReadyMoveInfoUseCaseModel?,
    ): NextResult {
        return when (touchAction) {
            is MoveTarget.Board -> {
                val holdPiece = holdMove?.hold
                val nextPosition = touchAction.position
                if (holdPiece != null && holdMove.hintList.contains(nextPosition)) {
                    setMove(board, stand, nextPosition, turn, holdPiece)
                } else {
                    setHintPosition(board, touchAction, turn)
                }
            }

            is MoveTarget.Stand -> {
                setHintPosition(board, touchAction, turn)
            }
        }
    }

    override fun setEvolution(
        board: Board,
        position: Position,
    ): Board {
        board.updatePieceEvolution(position)
        val key = logRepository.getLatestKey()
        logRepository.fixLogByEvolution(key)

        return board
    }

    private fun setHintPosition(
        board: Board,
        touchAction: MoveTarget,
        turn: Turn
    ): NextResult.Hint {
        val hintPositionList = when (touchAction) {
            is MoveTarget.Board -> {
                board.searchMoveBy(touchAction.position, turn)
            }

            is MoveTarget.Stand -> {
                board.searchPutBy(touchAction.piece, turn)
            }
        }

        return NextResult.Hint(
            hintPositionList = hintPositionList,
        )
    }

    private fun setMove(
        board: Board,
        stand: Stand,
        position: Position,
        turn: Turn,
        hold: MoveTarget,
    ): NextResult {
        val (newBoard, newStand) = when (hold) {
            is MoveTarget.Board -> {
                gameService.movePieceByPosition(board, stand, hold.position, position)
            }

            is MoveTarget.Stand -> {
                gameService.putPieceByStand(board, stand, turn, hold.piece, position)
            }
        }
        val takePiece = board.getPieceOrNullByPosition(position)?.let {
            if (it.turn != turn) {
                it.piece
            } else {
                null
            }
        }
        val isEvolution = board.getPieceOrNullByPosition(position)?.let {
            if (it.turn == turn) {
                it.piece is Piece.Reverse
            } else {
                false
            }
        } ?: false
        addLog(
            turn = turn,
            moveTarget = hold,
            afterPosition = position,
            isEvolution = isEvolution,
            takePiece = takePiece,
        )
        val opponentTurn = turn.getOpponentTurn()
        val nextTurn = turn.changeNextTurn()
        if (checkGameSet(newBoard, position, opponentTurn)) {
            return NextResult.Move.Win(
                board = newBoard,
                stand = newStand,
                nextTurn = nextTurn,
            )
        }

        if (hold is MoveTarget.Board) {
            val cellStatus = board.getPieceOrNullByPosition(hold.position)
            val piece = cellStatus?.piece as? Piece.Surface
            if (piece != null) {
                if (piece.shouldEvolution(board, position, cellStatus.turn)) {
                    newBoard.updatePieceEvolution(position)
                } else if (board.checkPieceEvolution(hold.position, position)) {
                    return NextResult.Move.ChooseEvolution(
                        board = newBoard,
                        stand = newStand,
                        nextTurn = nextTurn,
                    )
                }
            }
        }

        return if (gameService.isCheckmate(newBoard, stand, nextTurn)) {
            NextResult.Move.Win(
                board = newBoard,
                stand = newStand,
                nextTurn = nextTurn,
            )
        } else {
            NextResult.Move.Only(
                board = newBoard,
                stand = newStand,
                nextTurn = nextTurn,
            )
        }
    }

    private fun addLog(
        turn: Turn,
        moveTarget: MoveTarget,
        afterPosition: Position,
        isEvolution: Boolean,
        takePiece: Piece?,
    ) {
        val log = Log(
            turn = turn,
            moveTarget = moveTarget,
            afterPosition = afterPosition,
            isEvolution = isEvolution,
            takePiece = takePiece,
        )
        val data = logRepository.getLatestKey()

        logRepository.addLog(data, log)
    }

    private fun checkGameSet(board: Board, position: Position, turn: Turn): Boolean {
        if (gameRuleRepository.getIsFirstCheckEndRule()) {
            return board.isCheckByTurn(turn)
        }

        return board.isKingCellBy(position, turn)
    }
}
