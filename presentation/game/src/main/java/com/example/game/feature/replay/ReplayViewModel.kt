package com.example.game.feature.replay

import com.example.core.uilogic.BaseContract
import com.example.core.uilogic.BaseViewModel
import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.game.TimeLimit
import com.example.domainObject.game.log.MoveRecode
import com.example.domainObject.game.rule.Turn
import com.example.usecaseinterface.usecase.ReplayUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ReplayViewModel @Inject constructor(
    private val useCase: ReplayUseCase,
) : BaseViewModel<ReplayViewModel.UiState, ReplayViewModel.Effect>() {

    override fun initState(): UiState {
        return  UiState(
            board = Board(),
            blackStand = Stand(),
            whiteStand = Stand(),
            log = emptyList(),
            blackTimeLimit = TimeLimit.INIT,
            whiteTimeLimit = TimeLimit.INIT,
            logNextIndex = 0,
        )
    }

    init {
        initBard()
    }

    private fun initBard() {
        val result = useCase.replayInit()
        setState {
            UiState(
                board = result.board,
                blackStand = result.blackStand,
                whiteStand = result.whiteStand,
                blackTimeLimit = result.blackTimeLimit,
                whiteTimeLimit = result.whiteTimeLimit,
                log = result.log ?: emptyList(),
                logNextIndex = 0,
            )
        }
    }

    fun tapLeft() {
        val logIndex = state.value.logNextIndex - 1
        if (logIndex < 0) return
        val log = state.value.log[logIndex]
        val stand = when (log.turn) {
            Turn.Normal.Black -> state.value.blackStand
            Turn.Normal.White -> state.value.whiteStand
        }
        val result = useCase.goBack(state.value.board, stand, log)
        setState {
            copy(
                board = result.board,
                logNextIndex = logIndex,
                blackStand = if (log.turn == Turn.Normal.Black) result.stand else state.value.blackStand,
                whiteStand = if (log.turn == Turn.Normal.White) result.stand else state.value.whiteStand,
            )
        }
    }

    fun tapRight() {
        val logIndex = state.value.logNextIndex
        if (state.value.log.size <= logIndex) return
        val log = state.value.log[logIndex]
        val stand = when (log.turn) {
            Turn.Normal.Black -> state.value.blackStand
            Turn.Normal.White -> state.value.whiteStand
        }
        val result = useCase.goNext(state.value.board, stand, log)
        setState {
            copy(
                board = result.board,
                logNextIndex = logIndex + 1,
                blackStand = if (log.turn == Turn.Normal.Black) result.stand else state.value.blackStand,
                whiteStand = if (log.turn == Turn.Normal.White) result.stand else state.value.whiteStand,
            )
        }
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
        val blackTimeLimit: TimeLimit,
        val whiteTimeLimit: TimeLimit,
        val log: List<MoveRecode>,
        val logNextIndex: Int,
    ): BaseContract.State

    sealed interface Effect : BaseContract.Effect
}
