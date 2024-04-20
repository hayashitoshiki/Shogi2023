package com.example.game.feature.game

import androidx.lifecycle.viewModelScope
import com.example.core.uilogic.BaseContract
import com.example.core.uilogic.BaseViewModel
import com.example.entity.game.MoveTarget
import com.example.entity.game.board.Board
import com.example.entity.game.board.Position
import com.example.entity.game.board.Stand
import com.example.entity.game.piece.Piece
import com.example.entity.game.rule.Turn
import com.example.game.util.mapper.toUseCaseModel
import com.example.game.util.model.ReadyMoveInfoUiModel
import com.example.usecase.usecaseinterface.GameUseCase
import com.example.usecase.usecaseinterface.model.ReadyMoveInfoUseCaseModel
import com.example.usecase.usecaseinterface.model.result.NextResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val useCase: GameUseCase,
) : BaseViewModel <GameViewModel.UiState, GameViewModel.Effect>() {

    init {
        initBard()
    }

    override fun initState(): UiState {
        return  UiState(
            board = Board(),
            blackStand = Stand(),
            whiteStand = Stand(),
            turn = Turn.Normal.Black,
            readyMoveInfo = null,
        )
    }

    private fun initBard() {
        val result = useCase.gameInit()
        setState {
            UiState(
                board = result.board,
                blackStand = result.blackStand,
                whiteStand = result.whiteStand,
                turn = result.turn,
                readyMoveInfo = null,
            )
        }
    }

    fun tapBoard(position: Position) {
        val touchAction = MoveTarget.Board(position)
        tapAction(touchAction)
    }

    fun tapStand(piece: Piece, turn: Turn) {
        if (turn != state.value.turn) {
            setState {
                copy(
                    readyMoveInfo = null,
                )
            }
            return
        }

        val result = useCase.useStandPiece(
            board = state.value.board,
            piece = piece,
            turn = turn,
        )
        updateUiStateFromNextResult(result, MoveTarget.Stand(piece))
    }

    fun tapLoseButton(turn: Turn) {
        val winner = when (turn) {
            Turn.Normal.Black -> Turn.Normal.White
            Turn.Normal.White -> Turn.Normal.Black
        }
        viewModelScope.launch {
            setEffect { Effect.GameEnd.Win(winner) }
        }
    }

    private fun tapAction(touchAction: MoveTarget.Board) {
        val turn = state.value.turn
        val holdMove = state.value.readyMoveInfo?.toUseCaseModel()
        val result = if (holdMove != null && holdMove.hintList.contains(touchAction.position)) {
            val stand = when (state.value.turn) {
                Turn.Normal.Black -> state.value.blackStand
                Turn.Normal.White -> state.value.whiteStand
            }
            when (holdMove) {
                is ReadyMoveInfoUseCaseModel.Board -> {
                    useCase.movePiece(
                        board = state.value.board,
                        stand = stand,
                        touchAction = touchAction,
                        turn = turn,
                        holdMove = holdMove,
                    )
                }

                is ReadyMoveInfoUseCaseModel.Stand -> {
                    useCase.putStandPiece(
                        board = state.value.board,
                        stand = stand,
                        touchAction = touchAction,
                        turn = turn,
                        holdMove = holdMove,
                    )
                }
            }
        } else {
            useCase.useBoardPiece(
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
                        )
                    )
                }
            }

            is NextResult.Move.Only -> {
                setMoved(result)
            }

            is NextResult.Move.ChooseEvolution -> {
                setMoved(result)
                viewModelScope.launch {
                    if (touchAction !is MoveTarget.Board) return@launch
                    setEffect { Effect.Evolution(touchAction.position) }
                }
            }

            is NextResult.Move.Win -> {
                setMoved(result)
                viewModelScope.launch {
                    setEffect { Effect.GameEnd.Win(turn) }
                }
            }

            is NextResult.Move.Drown -> {
                setMoved(result)
                viewModelScope.launch {
                    setEffect { Effect.GameEnd.Draw }
                }
            }
        }
    }

    fun setEvolution(position: Position, isEvolution: Boolean) {
        val turn = state.value.turn
        val stand = when (turn) {
            Turn.Normal.Black -> state.value.whiteStand
            Turn.Normal.White -> state.value.blackStand
        }
        val result = useCase.setEvolution(
            turn = turn,
            board = state.value.board,
            stand = stand,
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
            viewModelScope.launch {
                setEffect { Effect.GameEnd.Win(turn) }
            }
        }
    }

    private fun setMoved(result: NextResult.Move) {
        setState {
            when (state.value.turn) {
                Turn.Normal.Black -> {
                    copy(
                        board = result.board,
                        blackStand = result.stand,
                        turn = result.nextTurn,
                        readyMoveInfo = null,
                    )
                }

                Turn.Normal.White -> {
                    copy(
                        board = result.board,
                        whiteStand = result.stand,
                        turn = result.nextTurn,
                        readyMoveInfo = null,
                    )
                }
            }
        }
    }

    /**
     * 画面状態
     *
     * @property board 将棋盤
     * @property blackStand 先手の持ち駒
     * @property whiteStand 後手の持ち駒
     * @property turn 手番
     * @property readyMoveInfo 動かそうとしている駒の情報
     */
    data class UiState(
        val board: Board,
        val blackStand: Stand,
        val whiteStand: Stand,
        val turn: Turn,
        val readyMoveInfo: ReadyMoveInfoUiModel?,
    ): BaseContract.State

    sealed interface Effect: BaseContract.Effect {

        /**
         * ゲーム終了
         *
         */
        sealed interface GameEnd : Effect {

            /**
             * 勝利
             */
            data class Win(val turn: Turn) : GameEnd

            /**
             * 引き分け
             */
            data object Draw : GameEnd
        }

        /**
         * 成り判定
         *
         * @property position 成る判定をする駒のマス
         */
        data class Evolution(val position: Position) : Effect
    }
}
