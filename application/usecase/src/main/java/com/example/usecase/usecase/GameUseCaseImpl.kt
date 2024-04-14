package com.example.usecase.usecase

import com.example.entity.game.Log
import com.example.entity.game.MoveTarget
import com.example.entity.game.board.Board
import com.example.entity.game.board.EvolutionCheckState
import com.example.entity.game.board.Position
import com.example.entity.game.board.Stand
import com.example.entity.game.piece.Piece
import com.example.entity.game.rule.Turn
import com.example.extention.changeNextTurn
import com.example.extention.checkPieceEvolution
import com.example.extention.getOpponentTurn
import com.example.extention.searchMoveBy
import com.example.extention.setUp
import com.example.extention.updatePieceEvolution
import com.example.repository.repositoryinterface.GameRepository
import com.example.repository.repositoryinterface.GameRuleRepository
import com.example.repository.repositoryinterface.LogRepository
import com.example.serviceinterface.GameService
import com.example.usecase.usecaseinterface.GameUseCase
import com.example.usecase.usecaseinterface.model.ReadyMoveInfoUseCaseModel
import com.example.usecase.usecaseinterface.model.result.GameInitResult
import com.example.usecase.usecaseinterface.model.result.NextResult
import com.example.usecase.usecaseinterface.model.result.SetEvolutionResult
import java.time.LocalDateTime
import javax.inject.Inject

class GameUseCaseImpl @Inject constructor(
    private val logRepository: LogRepository,
    private val gameRuleRepository: GameRuleRepository,
    private val gameRepository: GameRepository,
    private val gameService: GameService,
) : GameUseCase {

    override fun gameInit(): GameInitResult {
        val rule = gameRuleRepository.getGameRule()
        val board = Board.setUp(rule)
        val blackStand = Stand.setUp(rule.playersRule.blackRule)
        val whiteStand = Stand.setUp(rule.playersRule.whiteRule)
        val now = LocalDateTime.now()
        logRepository.createGameLog(now)

        return GameInitResult(
            board = board,
            blackStand = blackStand,
            whiteStand = whiteStand,
            turn = Turn.Normal.Black,
        )
    }

    override fun movePiece(
        board: Board,
        stand: Stand,
        turn: Turn,
        touchAction: MoveTarget.Board,
        holdMove: ReadyMoveInfoUseCaseModel.Board,
    ): NextResult {
        val holdPiece = holdMove.hold
        val nextPosition = touchAction.position
        return setMove(board, stand, nextPosition, turn, holdPiece)
    }

    override fun putStandPiece(
        board: Board,
        stand: Stand,
        turn: Turn,
        touchAction: MoveTarget.Board,
        holdMove: ReadyMoveInfoUseCaseModel.Stand,
    ): NextResult {
        val holdPiece = holdMove.hold
        val nextPosition = touchAction.position
        return setMove(board, stand, nextPosition, turn, holdPiece)
    }

    override fun useBoardPiece(
        board: Board,
        turn: Turn,
        position: Position,
    ): NextResult {
        val touchAction = MoveTarget.Board(position)
        return setHintPosition(board, touchAction, turn)
    }

    override fun useStandPiece(
        board: Board,
        turn: Turn,
        piece: Piece,
    ): NextResult.Hint {
        val touchAction = MoveTarget.Stand(piece)
        return setHintPosition(board, touchAction, turn)
    }

    override fun setEvolution(
        turn: Turn,
        board: Board,
        stand: Stand,
        position: Position,
        isEvolution: Boolean,
    ): SetEvolutionResult {
        if (isEvolution) {
            board.updatePieceEvolution(position)
            val key = logRepository.getLatestKey()
            logRepository.fixLogByEvolution(key)
        }
        val rule = gameRuleRepository.getGameRule()
        val isWin = gameService.checkGameSet(board, stand, turn, rule)
        val isDrown = checkDraw(board)

        return SetEvolutionResult(
            board = board,
            isWin = isWin,
            nextTurn = turn.getOpponentTurn(),
            isDraw = isDrown,
        )
    }

    private fun setHintPosition(
        board: Board,
        touchAction: MoveTarget,
        turn: Turn
    ): NextResult.Hint {
        val hintPositionList = when (touchAction) {
            is MoveTarget.Board -> board.searchMoveBy(touchAction.position, turn)
            is MoveTarget.Stand -> gameService.searchPutBy(touchAction.piece, board, turn)
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
        val takePiece = board.getPieceOrNullByPosition(position)
            ?.takeIf { it.turn != turn }
            ?.piece
        val isEvolution = board.getPieceOrNullByPosition(position)
            ?.let { it.turn == turn && it.piece is Piece.Reverse }
            ?: false
        addLog(
            turn = turn,
            moveTarget = hold,
            afterPosition = position,
            isEvolution = isEvolution,
            takePiece = takePiece,
        )

        if (hold is MoveTarget.Board) {
            val cellStatus = board.getPieceOrNullByPosition(hold.position)
            val piece = cellStatus?.piece as? Piece.Surface
            if (piece != null) {
                when (board.checkPieceEvolution(piece, hold.position, position, cellStatus.turn)) {
                    EvolutionCheckState.Should -> newBoard.updatePieceEvolution(position)
                    EvolutionCheckState.No -> Unit
                    EvolutionCheckState.Choose -> {
                        return NextResult.Move.ChooseEvolution(
                            board = newBoard,
                            stand = newStand,
                            nextTurn = turn,
                        )
                    }
                }
            }
        }

        val nextTurn = turn.changeNextTurn()
        val rule = gameRuleRepository.getGameRule()
        return if (gameService.checkGameSet(newBoard, stand, turn, rule)) {
            NextResult.Move.Win(
                board = newBoard,
                stand = newStand,
                nextTurn = nextTurn,
            )
        } else {
            if (checkDraw(newBoard)) {
                NextResult.Move.Drown(
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

    /**
     * 千日手判定
     *
     * @param board 現在の局面
     * @return 千日手か
     */
    private fun checkDraw(board: Board): Boolean {
        val boardLog = gameRepository.getBoardLogs()
        return if (gameService.checkDraw(boardLog, board)) {
            true
        } else {
            gameRepository.setBoardLog(board)
            false
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
}
