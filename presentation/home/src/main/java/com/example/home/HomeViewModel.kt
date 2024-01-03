package com.example.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.entity.game.rule.PieceSetUpRule
import com.example.usecase.usecaseinterface.HomeUseCase
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
class HomeViewModel @Inject constructor(
    private val useCase: HomeUseCase,
) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState> = MutableStateFlow(
        UiState(
            rule = PieceSetUpRule.Normal.NoHande,
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val mutableGameStartEffect: MutableSharedFlow<Effect.GameStart> = MutableSharedFlow()
    val gameStartEffect: SharedFlow<Effect.GameStart> = mutableGameStartEffect.asSharedFlow()

    fun changePieceHande(pieceHande: PieceSetUpRule.Normal) {
        _uiState.value = uiState.value.copy(
            rule = pieceHande,
        )
    }

    fun onGameStartClick() {
        // リポジトリ登録
        useCase.setGameRule(uiState.value.rule)
        viewModelScope.launch {
            mutableGameStartEffect.emit(Effect.GameStart)
        }
    }

    /**
     * 画面状態
     *
     * @property rule ルール
     */
    data class UiState(
        val rule: PieceSetUpRule.Normal,
    )

    sealed interface Effect {

        /**
         * 対局開始
         */
        data object GameStart : Effect
    }
}