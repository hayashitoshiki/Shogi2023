package com.example.usecase.usecase

import com.example.entity.game.board.Board
import com.example.entity.game.board.Position
import com.example.entity.game.board.Stand
import com.example.entity.game.rule.PieceSetUpRule
import com.example.entity.game.rule.Turn
import com.example.extention.changeNextTurn
import com.example.extention.isKingCellBy
import com.example.service.GameService
import com.example.usecase.usecaseinterface.GameUseCase
import com.example.usecase.usecaseinterface.model.MoveUseCaseModel
import com.example.usecase.usecaseinterface.model.TouchActionUseCaseModel
import com.example.usecase.usecaseinterface.model.result.GameInitResult
import com.example.usecase.usecaseinterface.model.result.NextResult
import javax.inject.Inject

class GameUseCaseImpl @Inject constructor() : GameUseCase {
    private val gameService = GameService()

    override fun gameInit(pieceSetUpRule: PieceSetUpRule): GameInitResult {
        val board = gameService.setUpBoard(pieceSetUpRule)
        val blackStand = Stand()
        val whiteStand = Stand()

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
        touchAction: TouchActionUseCaseModel,
        holdMove: MoveUseCaseModel?,
    ): NextResult {
        return when (touchAction) {
            is TouchActionUseCaseModel.Board -> {
                if (holdMove?.hold != null) {
                    val nextPosition = touchAction.position
                    if (holdMove.hintList.contains(nextPosition)) {
                        setMove(board, stand, nextPosition, turn, holdMove.hold)
                    } else {
                        setHintPosition(board, touchAction, turn)
                    }
                } else {
                    setHintPosition(board, touchAction, turn)
                }
            }

            is TouchActionUseCaseModel.Stand -> {
                setHintPosition(board, touchAction, turn)
            }
        }
    }

    override fun setEvolution(
        board: Board,
        position: Position,
    ): Board {
        gameService.pieceEvolution(board, position)
        return board
    }

    private fun setHintPosition(
        board: Board,
        touchAction: TouchActionUseCaseModel,
        turn: Turn
    ): NextResult.Hint {
        val hintPositionList = when (touchAction) {
            is TouchActionUseCaseModel.Board -> {
                gameService.searchMoveBy(board, touchAction.position, turn)
            }

            is TouchActionUseCaseModel.Stand -> {
                gameService.searchPutBy(board, touchAction.piece, turn)
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
        hold: TouchActionUseCaseModel,
    ): NextResult {
        val opponentTurn = when (turn) {
            Turn.Normal.Black -> Turn.Normal.White
            Turn.Normal.White -> Turn.Normal.Black
        }
        val isGetKing = board.isKingCellBy(position, opponentTurn)
        val (newBoard, newStand) = when (hold) {
            is TouchActionUseCaseModel.Board -> {
                gameService.movePieceByPosition(board, stand, hold.position, position)
            }

            is TouchActionUseCaseModel.Stand -> {
                gameService.putPieceByStand(board, stand, turn, hold.piece, position)
            }
        }
        val nextTurn = turn.changeNextTurn()
        if (isGetKing) {
            return NextResult.Move.Win(
                board = newBoard,
                stand = newStand,
                nextTurn = nextTurn,
            )
        }

        if (hold is TouchActionUseCaseModel.Board) {
            if (gameService.shouldPieceEvolution(board, hold.position, position)) {
                gameService.pieceEvolution(newBoard, position)
            } else if (gameService.checkPieceEvolution(board, hold.position, position)) {
                return NextResult.Move.ChooseEvolution(
                    board = newBoard,
                    stand = newStand,
                    nextTurn = nextTurn,
                )
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
}
