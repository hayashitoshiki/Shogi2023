package com.example.home.game

import androidx.lifecycle.ViewModel
import com.example.entity.game.board.Board
import com.example.entity.game.board.Stand
import com.example.entity.game.rule.PieceSetUpRule
import com.example.usecase.usecaseinterface.GameUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class GameViewModel @Inject constructor(
    private val useCase: GameUseCase,
) : ViewModel() {

    private val _uiState: MutableStateFlow<GameUiState> = MutableStateFlow(
        GameUiState(
            board = Board(),
            blackStand = Stand(),
            whiteStand = Stand(),
        )
    )
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        initBard()
    }

    fun initBard() {
        val result = useCase.gameInit(PieceSetUpRule.Normal.NoHande)
        _uiState.value = GameUiState(
            board = result.board,
            blackStand = result.blackStand,
            whiteStand = result.whiteStand,
        )
    }

}

data class GameUiState(
    val board: Board,
    val blackStand: Stand,
    val whiteStand: Stand,
)
