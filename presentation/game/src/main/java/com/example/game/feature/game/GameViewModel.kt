package com.example.game.feature.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val useCase: GameUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState(
            board = Board(),
            blackStand = Stand(),
            whiteStand = Stand(),
            turn = Turn.Normal.Black,
            readyMoveInfo = null,
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    private val mutableEffect: Channel<Effect> = Channel()
    val effect: Flow<Effect> = mutableEffect.receiveAsFlow()

    init {
        initBard()
    }

    private fun initBard() {
        val result = useCase.gameInit()
        _uiState.value = UiState(
            board = result.board,
            blackStand = result.blackStand,
            whiteStand = result.whiteStand,
            turn = result.turn,
            readyMoveInfo = null,
        )
    }

    fun tapBoard(position: Position) {
        val touchAction = MoveTarget.Board(position)
        tapAction(touchAction)
    }

    fun tapStand(piece: Piece, turn: Turn) {
        if (turn != uiState.value.turn) {
            _uiState.value = uiState.value.copy(
                readyMoveInfo = null,
            )
            return
        }

        val result = useCase.useStandPiece(
            board = uiState.value.board,
            piece = piece,
            turn = turn,
        )
        _uiState.value = uiState.value.copy(
            readyMoveInfo = ReadyMoveInfoUiModel(
                hold = MoveTarget.Stand(piece),
                hintList = result.hintPositionList,
            )
        )
    }

    fun tapLoseButton(turn: Turn) {
        val winner = when (turn) {
            Turn.Normal.Black -> Turn.Normal.White
            Turn.Normal.White -> Turn.Normal.Black
        }
        viewModelScope.launch {
            mutableEffect.send(Effect.GameEnd.Win(winner))
        }
    }

    private fun tapAction(touchAction: MoveTarget.Board) {
        val turn = uiState.value.turn
        val holdMove = uiState.value.readyMoveInfo?.toUseCaseModel()
        val result = if (holdMove != null && holdMove.hintList.contains(touchAction.position)) {
            val stand = when (uiState.value.turn) {
                Turn.Normal.Black -> uiState.value.blackStand
                Turn.Normal.White -> uiState.value.whiteStand
            }
            when (holdMove) {
                is ReadyMoveInfoUseCaseModel.Board -> {
                    useCase.movePiece(
                        board = uiState.value.board,
                        stand = stand,
                        touchAction = touchAction,
                        turn = turn,
                        holdMove = holdMove,
                    )
                }

                is ReadyMoveInfoUseCaseModel.Stand -> {
                    useCase.putStandPiece(
                        board = uiState.value.board,
                        stand = stand,
                        touchAction = touchAction,
                        turn = turn,
                        holdMove = holdMove,
                    )
                }
            }
        } else {
            useCase.useBoardPiece(
                board = uiState.value.board,
                turn = turn,
                position = touchAction.position,
            )
        }

        when (result) {
            is NextResult.Hint -> {
                _uiState.value = uiState.value.copy(
                    readyMoveInfo = ReadyMoveInfoUiModel(
                        hold = touchAction,
                        hintList = result.hintPositionList,
                    )
                )
            }

            is NextResult.Move.Only -> {
                setMoved(result)
            }

            is NextResult.Move.ChooseEvolution -> {
                setMoved(result)
                viewModelScope.launch {
                    if (touchAction !is MoveTarget.Board) return@launch
                    mutableEffect.send(Effect.Evolution(touchAction.position))
                }
            }

            is NextResult.Move.Win -> {
                setMoved(result)
                viewModelScope.launch {
                    mutableEffect.send(Effect.GameEnd.Win(turn))
                }
            }

            is NextResult.Move.Drown -> {
                setMoved(result)
                viewModelScope.launch {
                    mutableEffect.send(Effect.GameEnd.Draw)
                }
            }
        }
    }

    fun setEvolution(position: Position, isEvolution: Boolean) {
        val turn = uiState.value.turn
        val stand = when (turn) {
            Turn.Normal.Black -> uiState.value.whiteStand
            Turn.Normal.White -> uiState.value.blackStand
        }
        val result = useCase.setEvolution(
            turn = turn,
            board = uiState.value.board,
            stand = stand,
            position = position,
            isEvolution = isEvolution,
        )
        _uiState.value = uiState.value.copy(
            board = result.board,
            turn = result.nextTurn,
        )
        if (result.isWin) {
            viewModelScope.launch {
                mutableEffect.send(Effect.GameEnd.Win(turn))
            }
        }
    }

    private fun setMoved(result: NextResult.Move) {
        _uiState.value = when (uiState.value.turn) {
            Turn.Normal.Black -> {
                uiState.value.copy(
                    board = result.board,
                    blackStand = result.stand,
                    turn = result.nextTurn,
                    readyMoveInfo = null,
                )
            }

            Turn.Normal.White -> {
                uiState.value.copy(
                    board = result.board,
                    whiteStand = result.stand,
                    turn = result.nextTurn,
                    readyMoveInfo = null,
                )
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
    )

    sealed interface Effect {

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
