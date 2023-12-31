package com.example.home.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.entity.game.board.Board
import com.example.entity.game.board.Position
import com.example.entity.game.board.Stand
import com.example.entity.game.piece.Piece
import com.example.entity.game.rule.PieceSetUpRule
import com.example.entity.game.rule.Turn
import com.example.home.game.mapper.toUseCaseModel
import com.example.home.game.model.ReadyMoveInfoUiModel
import com.example.home.game.model.TouchActionUiModel
import com.example.usecase.usecaseinterface.GameUseCase
import com.example.usecase.usecaseinterface.model.result.NextResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
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

    private val mutableGameEndEffect: MutableSharedFlow<Effect.GameEnd> = MutableSharedFlow()
    val gameEndEffect: SharedFlow<Effect.GameEnd> = mutableGameEndEffect.asSharedFlow()
    private val mutableEvolutionEffect: MutableSharedFlow<Effect.Evolution> = MutableSharedFlow()
    val evolutionEffect: SharedFlow<Effect.Evolution> = mutableEvolutionEffect.asSharedFlow()

    init {
        initBard()
    }

    fun initBard() {
        val result = useCase.gameInit(PieceSetUpRule.Normal.NoHande)
        _uiState.value = UiState(
            board = result.board,
            blackStand = result.blackStand,
            whiteStand = result.whiteStand,
            turn = result.turn,
            readyMoveInfo = null,
        )
    }

    fun tapBoard(position: Position) {
        val touchAction = TouchActionUiModel.Board(position)
        tapAction(touchAction)
    }

    fun tapStand(piece: Piece, turn: Turn) {
        if (turn != uiState.value.turn) {
            _uiState.value = uiState.value.copy(
                readyMoveInfo = null,
            )
            return
        }
        val touchAction = TouchActionUiModel.Stand(piece)
        tapAction(touchAction)
    }

    fun tapLoseButton(turn: Turn) {
        val winner = when (turn) {
            Turn.Normal.Black -> Turn.Normal.White
            Turn.Normal.White -> Turn.Normal.Black
        }
        viewModelScope.launch {
            mutableGameEndEffect.emit(Effect.GameEnd(winner))
        }
    }

    private fun tapAction(touchAction: TouchActionUiModel) {
        val stand = when (uiState.value.turn) {
            Turn.Normal.Black -> uiState.value.blackStand
            Turn.Normal.White -> uiState.value.whiteStand
        }
        val turn = uiState.value.turn
        val result = useCase.next(
            board = uiState.value.board,
            stand = stand,
            touchAction = touchAction.toUseCaseModel(),
            turn = turn,
            holdMove = uiState.value.readyMoveInfo?.toUseCaseModel(),
        )

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
                    if (touchAction !is TouchActionUiModel.Board) return@launch
                    mutableEvolutionEffect.emit(Effect.Evolution(touchAction.position))
                }
            }

            is NextResult.Move.Win -> {
                setMoved(result)
                viewModelScope.launch {
                    mutableGameEndEffect.emit(Effect.GameEnd(turn))
                }
            }
        }
    }

    fun setEvolution(position: Position) {
        val result = useCase.setEvolution(uiState.value.board, position)
        _uiState.value = uiState.value.copy(
            board = result,
        )
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
         */
        data class GameEnd(val turn: Turn) : Effect

        /**
         * 成り判定
         *
         * @property position 成る判定をする駒のマス
         */
        data class Evolution(val position: Position) : Effect
    }
}
