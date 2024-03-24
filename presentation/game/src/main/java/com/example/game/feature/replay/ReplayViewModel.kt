package com.example.game.feature.replay

import androidx.lifecycle.ViewModel
import com.example.entity.game.Log
import com.example.entity.game.board.Board
import com.example.entity.game.board.Stand
import com.example.entity.game.rule.Turn
import com.example.usecase.usecaseinterface.ReplayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ReplayViewModel @Inject constructor(
    private val useCase: ReplayUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState(
            board = Board(),
            blackStand = Stand(),
            whiteStand = Stand(),
            log = emptyList(),
            logNextIndex = 0,
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        initBard()
    }

    fun initBard() {
        val result = useCase.replayInit()
        _uiState.value = UiState(
            board = result.board,
            blackStand = result.blackStand,
            whiteStand = result.whiteStand,
            log = result.log ?: emptyList(),
            logNextIndex = 0,
        )
    }

    fun tagLeft() {
        val logIndex = uiState.value.logNextIndex - 1
        if (logIndex < 0) return
        val log = uiState.value.log[logIndex]
        val stand = when (log.turn) {
            Turn.Normal.Black -> uiState.value.blackStand
            Turn.Normal.White -> uiState.value.whiteStand
        }
        val result = useCase.goBack(uiState.value.board, stand, log)
        _uiState.value = uiState.value.copy(
            board = result.board,
            logNextIndex = logIndex,
            blackStand = if (log.turn == Turn.Normal.Black) result.stand else uiState.value.blackStand,
            whiteStand = if (log.turn == Turn.Normal.White) result.stand else uiState.value.whiteStand,
        )
    }

    fun tagRight() {
        val logIndex = uiState.value.logNextIndex
        if (uiState.value.log.size <= logIndex) return
        val log = uiState.value.log[logIndex]
        val stand = when (log.turn) {
            Turn.Normal.Black -> uiState.value.blackStand
            Turn.Normal.White -> uiState.value.whiteStand
        }
        val result = useCase.goNext(uiState.value.board, stand, log)
        _uiState.value = uiState.value.copy(
            board = result.board,
            logNextIndex = logIndex + 1,
            blackStand = if (log.turn == Turn.Normal.Black) result.stand else uiState.value.blackStand,
            whiteStand = if (log.turn == Turn.Normal.White) result.stand else uiState.value.whiteStand,
        )
    }

    /**
     * 画面状態
     *
     * @property board 将棋盤
     * @property blackStand 先手の持ち駒
     * @property whiteStand 後手の持ち駒
     * @property log 棋譜
     * @property logNextIndex 次に読み込む棋譜の位置
     */
    data class UiState(
        val board: Board,
        val blackStand: Stand,
        val whiteStand: Stand,
        val log: List<Log>,
        val logNextIndex: Int,
    )
}
