package com.example.game.feature.replay

import androidx.lifecycle.ViewModel
import com.example.entity.game.Log
import com.example.entity.game.board.Board
import com.example.entity.game.board.Stand
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class ReplayViewModel @Inject constructor(
) : ViewModel() {

    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState(
            board = Board(),
            blackStand = Stand(),
            whiteStand = Stand(),
            log = emptyList(),
            logIndex = 0,
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        initBard()
    }

    fun initBard() {
        // TODO 初期化処理
    }

    fun tagLeft() {
        // TODO １つ戻る
    }

    fun tagRight() {
        // TODO １つ進める
    }

    /**
     * 画面状態
     *
     * @property board 将棋盤
     * @property blackStand 先手の持ち駒
     * @property whiteStand 後手の持ち駒
     * @property log 棋譜
     * @property logIndex 現在のログの位置
     */
    data class UiState(
        val board: Board,
        val blackStand: Stand,
        val whiteStand: Stand,
        val log: List<Log>,
        val logIndex: Int,
    )
}
