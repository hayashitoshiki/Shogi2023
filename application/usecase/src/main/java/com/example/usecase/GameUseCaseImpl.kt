package com.example.usecase

import com.example.domainLogic.board.checkPieceEvolution
import com.example.domainLogic.board.searchMoveBy
import com.example.domainLogic.board.setUp
import com.example.domainLogic.board.updatePieceEvolution
import com.example.domainLogic.rule.changeNextTurn
import com.example.domainLogic.rule.getBeforeTurn
import com.example.domainLogic.rule.getOpponentTurn
import com.example.domainObject.game.log.MoveTarget
import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.EvolutionCheckState
import com.example.domainObject.game.board.Position
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.game.Seconds
import com.example.domainObject.game.game.TimeLimit
import com.example.domainObject.game.log.GameRecode
import com.example.domainObject.game.log.GameResult
import com.example.domainObject.game.log.MoveRecode
import com.example.domainObject.game.piece.Piece
import com.example.domainObject.game.rule.GameRule
import com.example.domainObject.game.rule.Turn
import com.example.repository.BoardRepository
import com.example.repository.GameRecodeRepository
import com.example.repository.GameRuleRepository
import com.example.serviceinterface.GameService
import com.example.usecaseinterface.model.ReadyMoveInfoUseCaseModel
import com.example.usecaseinterface.model.TimeLimitsUseCaseModel
import com.example.usecaseinterface.model.result.GameInitResult
import com.example.usecaseinterface.model.result.NextResult
import com.example.usecaseinterface.model.result.SetEvolutionResult
import com.example.usecaseinterface.usecase.GameUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

class GameUseCaseImpl @Inject constructor(
    private val gameRecodeRepository: GameRecodeRepository,
    private val gameRuleRepository: GameRuleRepository,
    private val boardRepository: BoardRepository,
    private val gameService: GameService,
    private val coroutineScope: CoroutineScope,
) : GameUseCase {

    private val mutableTurnStateFlow = MutableStateFlow<Turn?>(null)
    private val mutableTimeLimitsUseCaseModelStateFlow = MutableStateFlow<TimeLimitsUseCaseModel?>(null)
    private var timerJob: Job? = null

    init {
        mutableTurnStateFlow.filterNotNull().onEach { turn ->
            stopTimer()
            updateIfNeedByoyomi(turn.getBeforeTurn())
            startTimer(turn)
        }.launchIn(coroutineScope)
    }

    override fun observeUpdateTimeLimit(): StateFlow<TimeLimitsUseCaseModel?> =
        mutableTimeLimitsUseCaseModelStateFlow.asStateFlow()

    override fun gameStart() {
        changeTurn(turn = Turn.Normal.Black)
    }

    override fun gameEnd() {
        stopTimer()
    }

    private fun startTimer(turn: Turn) {
        timerJob?.cancel()
        timerJob = CoroutineScope(coroutineScope.coroutineContext).launch {
            val localTimeLimitsUseCaseModel = mutableTimeLimitsUseCaseModelStateFlow.value ?: return@launch
            countDownTimeLimit(
                timeLimit = localTimeLimitsUseCaseModel,
                getLimitTime = {
                    when (turn) {
                        Turn.Normal.Black -> it.blackTimeLimit.totalTime.millisecond
                        Turn.Normal.White -> it.whiteTimeLimit.totalTime.millisecond
                    }
                },
                updateTime = { timeLimits, countDownTime -> updateTotalTime(timeLimits, turn, countDownTime) },
            )
            countDownTimeLimit(
                timeLimit = localTimeLimitsUseCaseModel,
                getLimitTime = {
                    when (turn) {
                        Turn.Normal.Black -> it.blackTimeLimit.byoyomi.millisecond
                        Turn.Normal.White -> it.whiteTimeLimit.byoyomi.millisecond
                    }
                },
                updateTime = { it, countDownTime -> updateByoyomi(it, turn, countDownTime) },
            )
        }
    }

    private suspend fun countDownTimeLimit(
        timeLimit: TimeLimitsUseCaseModel,
        getLimitTime: (TimeLimitsUseCaseModel) -> Long,
        updateTime: (TimeLimitsUseCaseModel, Long) -> Unit,
    ) {
        val delayTime = 50L
        var countDownTime = getLimitTime(timeLimit)
        while (0 <= countDownTime) {
            delay(delayTime)
            countDownTime -= delayTime
            mutableTimeLimitsUseCaseModelStateFlow.value?.also {
                updateTime(it, countDownTime)
            }
        }
        mutableTimeLimitsUseCaseModelStateFlow.value?.also {
            updateTime(it, 0)
        }
    }

    private fun updateIfNeedByoyomi(turn: Turn) {
        mutableTimeLimitsUseCaseModelStateFlow.value?.let {
            when (turn) {
                Turn.Normal.Black -> {
                    if (it.blackTimeLimit.isByoyomi()) {
                        updateByoyomi(it, turn, it.blackTimeLimit.setting.byoyomi.millisecond)
                    }
                }
                Turn.Normal.White -> {
                    if (it.whiteTimeLimit.isByoyomi()) {
                        updateByoyomi(it, turn, it.whiteTimeLimit.setting.byoyomi.millisecond)
                    }
                }
            }
        }
    }

    private fun updateTotalTime(timeLimits: TimeLimitsUseCaseModel, turn: Turn, countDownTime: Long) {
        mutableTimeLimitsUseCaseModelStateFlow.value = when (turn) {
            Turn.Normal.Black -> timeLimits.copy(
                blackTimeLimit = timeLimits.blackTimeLimit.copy(
                    totalTime = Seconds.setMillisecond(countDownTime),
                ),
            )
            Turn.Normal.White -> timeLimits.copy(
                whiteTimeLimit = timeLimits.whiteTimeLimit.copy(
                    totalTime = Seconds.setMillisecond(countDownTime),
                ),
            )
        }
    }
    private fun updateByoyomi(timeLimits: TimeLimitsUseCaseModel, turn: Turn, countDownTotalTime: Long) {
        mutableTimeLimitsUseCaseModelStateFlow.value = when (turn) {
            Turn.Normal.Black -> timeLimits.copy(
                blackTimeLimit = timeLimits.blackTimeLimit.copy(
                    byoyomi = Seconds.setMillisecond(countDownTotalTime),
                ),
            )
            Turn.Normal.White -> timeLimits.copy(
                whiteTimeLimit = timeLimits.whiteTimeLimit.copy(
                    byoyomi = Seconds.setMillisecond(countDownTotalTime),
                ),
            )
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
    }

    private fun changeTurn(turn: Turn) {
        mutableTurnStateFlow.value = turn
    }

    override fun gameInit(): GameInitResult {
        val rule = gameRuleRepository.get()
        val timeLimitRule = rule.timeLimitRule
        val board = Board.setUp(rule.boardRule)
        val blackStand = Stand.setUp()
        val whiteStand = Stand.setUp()
        val blackTimeLimit = TimeLimit(timeLimitRule.blackTimeLimitRule)
        val whiteTimeLimit = TimeLimit(timeLimitRule.whiteTimeLimitRule)
        createGameRecode(rule)
        mutableTimeLimitsUseCaseModelStateFlow.value =
            TimeLimitsUseCaseModel(
                blackTimeLimit = blackTimeLimit,
                whiteTimeLimit = whiteTimeLimit,
            )

        return GameInitResult(
            board = board,
            blackStand = blackStand,
            whiteStand = whiteStand,
            blackTimeLimit = blackTimeLimit,
            whiteTimeLimit = whiteTimeLimit,
            turn = Turn.Normal.Black,
        )
    }

    private fun createGameRecode(rule: GameRule) {
        val gameRecode = GameRecode(
            date = LocalDateTime.now(),
            result = GameResult.Playing,
            rule = rule,
            moveRecodes = emptyList(),
        )
        gameRecodeRepository.set(gameRecode)
    }

    override fun movePiece(
        board: Board,
        blackStand: Stand,
        whiteStand: Stand,
        turn: Turn,
        touchAction: MoveTarget.Board,
        holdMove: ReadyMoveInfoUseCaseModel.Board,
    ): NextResult {
        val holdPiece = holdMove.hold
        val nextPosition = touchAction.position
        return setMove(board, blackStand, whiteStand, nextPosition, turn, holdPiece)
    }

    override fun putStandPiece(
        board: Board,
        blackStand: Stand,
        whiteStand: Stand,
        turn: Turn,
        touchAction: MoveTarget.Board,
        holdMove: ReadyMoveInfoUseCaseModel.Stand,
    ): NextResult {
        val holdPiece = holdMove.hold
        val nextPosition = touchAction.position
        return setMove(board, blackStand, whiteStand, nextPosition, turn, holdPiece)
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
        blackStand: Stand,
        whiteStand: Stand,
        position: Position,
        isEvolution: Boolean,
    ): SetEvolutionResult {
        if (isEvolution) {
            board.updatePieceEvolution(position)
            updateMoveRecodeEvolution()
        }
        val rule = gameRuleRepository.get()
        val stand = getStandByTUrn(turn, blackStand, whiteStand)
        val isWin = gameService.checkGameSet(board, stand, turn, rule)
        val isDrown = checkDraw(board)
        val nextTurn = turn.getOpponentTurn()
        if (isWin) {
            stopTimer()
        } else {
            changeTurn(nextTurn)
        }
        return SetEvolutionResult(
            board = board,
            isWin = isWin,
            nextTurn = nextTurn,
            isDraw = isDrown,
        )
    }

    private fun setHintPosition(
        board: Board,
        touchAction: MoveTarget,
        turn: Turn,
    ): NextResult.Hint {
        val hintPositionList = when (touchAction) {
            is MoveTarget.Board -> board.searchMoveBy(touchAction.position, turn)
            is MoveTarget.Stand -> gameService.searchPutBy(touchAction.piece, board, turn)
        }

        return NextResult.Hint(
            hintPositionList = hintPositionList,
        )
    }

    private fun updateMoveRecodeEvolution() {
        val gameRecode = gameRecodeRepository.getLast()
            ?.takeIf { it.moveRecodes.isNotEmpty() }
            ?: return
        val updatedMoveRecodes = gameRecode.moveRecodes
            .toMutableList()
            .apply { this[lastIndex] = last().copy(isEvolution = true) }
        val newGameRecode = gameRecode.copy(moveRecodes = updatedMoveRecodes)
        gameRecodeRepository.set(newGameRecode)
    }

    private fun setMove(
        board: Board,
        blackStand: Stand,
        whiteStand: Stand,
        position: Position,
        turn: Turn,
        hold: MoveTarget,
    ): NextResult {
        val stand = getStandByTUrn(turn, blackStand, whiteStand)
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

        val (newBlackStand, newWhiteStand) = when(turn) {
            Turn.Normal.Black -> newStand to whiteStand
            Turn.Normal.White -> blackStand to newStand
        }
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
                            blackStand = newBlackStand,
                            whiteStand = newWhiteStand,
                            nextTurn = turn,
                        )
                    }
                }
            }
        }

        val nextTurn = turn.changeNextTurn()
        val rule = gameRuleRepository.get()
        return if (gameService.checkGameSet(newBoard, stand, turn, rule)) {
            NextResult.Move.Win(
                board = newBoard,
                blackStand = newBlackStand,
                whiteStand = newWhiteStand,
                nextTurn = nextTurn,
            )
        } else {
            if (checkDraw(newBoard)) {
                NextResult.Move.Drown(
                    board = newBoard,
                    blackStand = newBlackStand,
                    whiteStand = newWhiteStand,
                    nextTurn = nextTurn,
                )
            } else {
                changeTurn(nextTurn)
                NextResult.Move.Only(
                    board = newBoard,
                    blackStand = newBlackStand,
                    whiteStand = newWhiteStand,
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
        val boardLog = boardRepository.get()
        return if (gameService.checkDraw(boardLog, board)) {
            true
        } else {
            boardRepository.set(board)
            false
        }
    }

    private fun getStandByTUrn(turn: Turn, blackStand: Stand, whiteStand: Stand): Stand {
        return when (turn) {
            Turn.Normal.Black -> blackStand
            Turn.Normal.White -> whiteStand
        }
    }

    private fun addLog(
        turn: Turn,
        moveTarget: MoveTarget,
        afterPosition: Position,
        isEvolution: Boolean,
        takePiece: Piece?,
    ) {
        val moveRecode = MoveRecode(
            turn = turn,
            moveTarget = moveTarget,
            afterPosition = afterPosition,
            isEvolution = isEvolution,
            takePiece = takePiece,
        )
        val gameRecode = gameRecodeRepository.getLast()
            ?.let { it.copy(moveRecodes = it.moveRecodes + moveRecode) }
            ?: return
        gameRecodeRepository.set(gameRecode)
    }
}
