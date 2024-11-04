package com.example.game.feature.game

import androidx.lifecycle.viewModelScope
import com.example.domainObject.game.log.MoveTarget
import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Position
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.game.TimeLimit
import com.example.domainObject.game.piece.Piece
import com.example.domainObject.game.rule.Turn
import com.example.game.util.mapper.toUseCaseModel
import com.example.game.util.model.ReadyMoveInfoUiModel
import com.example.core.uilogic.BaseContract
import com.example.core.uilogic.BaseViewModel
import com.example.domainObject.game.rule.Turn.Normal.Black.getOpponentTurn
import com.example.usecaseinterface.model.ReadyMoveInfoUseCaseModel
import com.example.usecaseinterface.model.TimeOverUseCaseModel
import com.example.usecaseinterface.model.result.NextResult
import com.example.usecaseinterface.usecase.GameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameUseCase: GameUseCase,
) : BaseViewModel<GameViewModel.UiState, GameViewModel.Effect, GameViewModel.Action>() {

    init {
        initBard()
        gameUseCase.gameStart()
        gameUseCase.observeUpdateTimeLimit().filterNotNull().onEach {
            setState {
                copy(
                    blackTimeLimit = it.blackTimeLimit,
                    whiteTimeLimit = it.whiteTimeLimit,
                )
            }

            val timeOver = it.timeOver
            if (timeOver is TimeOverUseCaseModel.TimeOver) {
                setWin(timeOver.turn.getOpponentTurn())
            }
        }.launchIn(viewModelScope)
    }

    override fun initState(): UiState {
        return UiState(
            board = Board(),
            blackStand = Stand(),
            whiteStand = Stand(),
            turn = Turn.Normal.Black,
            blackTimeLimit = TimeLimit.INIT,
            whiteTimeLimit = TimeLimit.INIT,
            readyMoveInfo = null,
        )
    }

    private fun initBard() {
        val result = gameUseCase.gameInit()
        setState {
            UiState(
                board = result.board,
                blackStand = result.blackStand,
                whiteStand = result.whiteStand,
                blackTimeLimit = result.blackTimeLimit,
                whiteTimeLimit = result.whiteTimeLimit,
                turn = result.turn,
                readyMoveInfo = null,
            )
        }
    }

    override fun callAction(action: Action) {
        when(action) {
            is Action.TapBoard -> updateBoard(action.position)
            is Action.TapStand -> updateStand(action.piece, action.turn)
            is Action.ClickLoseButton -> setLose(action.turn)
            is Action.ClickEvolutionConfirmDialogConfirmButton -> setEvolution(action.position, action.isEvolution)
            Action.ClickGameEndDialogHomeButton -> setEffect { Effect.NavigateHomeScreen }
            Action.ClickGameEndDialogReplayButton -> setEffect { Effect.NavigateReplayScreen }
        }
    }

    private fun updateBoard(position: Position) {
        val touchAction = MoveTarget.Board(position)
        tapAction(touchAction)
    }

    private fun updateStand(piece: Piece, turn: Turn) {
        if (turn != state.value.turn) return
        val result = gameUseCase.useStandPiece(
            board = state.value.board,
            piece = piece,
            turn = turn,
        )
        updateUiStateFromNextResult(result, MoveTarget.Stand(piece))
    }

    private fun setLose(turn: Turn) {
        setWin(turn.getOpponentTurn())
    }

    private fun tapAction(touchAction: MoveTarget.Board) {
        val turn = state.value.turn
        val holdMove = state.value.readyMoveInfo?.toUseCaseModel()
        val result = if (holdMove != null && holdMove.hintList.contains(touchAction.position)) {
            when (holdMove) {
                is ReadyMoveInfoUseCaseModel.Board -> {
                    gameUseCase.movePiece(
                        board = state.value.board,
                        blackStand = state.value.blackStand,
                        whiteStand = state.value.whiteStand,
                        touchAction = touchAction,
                        turn = turn,
                        holdMove = holdMove,
                    )
                }

                is ReadyMoveInfoUseCaseModel.Stand -> {
                    gameUseCase.putStandPiece(
                        board = state.value.board,
                        blackStand = state.value.blackStand,
                        whiteStand = state.value.whiteStand,
                        touchAction = touchAction,
                        turn = turn,
                        holdMove = holdMove,
                    )
                }
            }
        } else {
            gameUseCase.useBoardPiece(
                board = state.value.board,
                turn = turn,
                position = touchAction.position,
            )
        }
        updateUiStateFromNextResult(result, touchAction)
    }

    private fun updateUiStateFromNextResult(result: NextResult, touchAction: MoveTarget) {
        val turn = state.value.turn
        when (result) {
            is NextResult.Hint -> {
                setState {
                    copy(
                        readyMoveInfo = ReadyMoveInfoUiModel(
                            hold = touchAction,
                            hintList = result.hintPositionList,
                        ),
                    )
                }
            }

            is NextResult.Move.Only -> {
                setMoved(result)
            }

            is NextResult.Move.ChooseEvolution -> {
                setMoved(result)
                if (touchAction !is MoveTarget.Board) return
                setEffect { Effect.ShowEvolutionDialog(touchAction.position) }
            }

            is NextResult.Move.Win -> {
                setMoved(result)
                setWin(turn)
            }

            is NextResult.Move.Drown -> {
                setMoved(result)
                setEffect { Effect.ShowGameEndDialog.Draw }
            }
        }
    }

    private fun setEvolution(position: Position, isEvolution: Boolean) {
        val turn = state.value.turn
        val result = gameUseCase.setEvolution(
            turn = turn,
            board = state.value.board,
            blackStand = state.value.blackStand,
            whiteStand = state.value.whiteStand,
            position = position,
            isEvolution = isEvolution,
        )
        setState {
            copy(
                board = result.board,
                turn = result.nextTurn,
            )
        }
        if (result.isWin) {
            setWin(turn)
        }
    }

    private fun setMoved(result: NextResult.Move) {
        setState {
            copy(
                board = result.board,
                blackStand = result.blackStand,
                whiteStand = result.whiteStand,
                turn = result.nextTurn,
                readyMoveInfo = null,
            )
        }
    }

    private fun setWin(turn: Turn) {
        gameUseCase.gameEnd()
        setEffect { Effect.ShowGameEndDialog.Win(turn) }
    }

    /**
     * 画面状態
     *
     * @property board 将棋盤
     * @property blackStand 先手の持ち駒
     * @property whiteStand 後手の持ち駒
     * @property blackTimeLimit 先手の持ち時間
     * @property whiteTimeLimit 後手の持ち時間
     * @property turn 手番
     * @property readyMoveInfo 動かそうとしている駒の情報
     */
    data class UiState(
        val board: Board,
        val blackStand: Stand,
        val whiteStand: Stand,
        val blackTimeLimit: TimeLimit,
        val whiteTimeLimit: TimeLimit,
        val turn: Turn,
        val readyMoveInfo: ReadyMoveInfoUiModel?,
    ) : BaseContract.State

    sealed interface Effect : BaseContract.Effect {
        sealed interface ShowGameEndDialog : Effect {
            data class Win(val turn: Turn) : ShowGameEndDialog
            data object Draw : ShowGameEndDialog
        }
        data class ShowEvolutionDialog(val position: Position) : Effect
        data object NavigateHomeScreen: Effect
        data object NavigateReplayScreen: Effect
    }

    sealed interface Action: BaseContract.Action {
        data class TapBoard(val position: Position): Action
        data class TapStand(val piece: Piece, val turn: Turn): Action
        data class ClickLoseButton(val turn: Turn): Action
        data class ClickEvolutionConfirmDialogConfirmButton(val position: Position, val isEvolution: Boolean): Action
        data object ClickGameEndDialogHomeButton: Action
        data object ClickGameEndDialogReplayButton: Action
    }
}
