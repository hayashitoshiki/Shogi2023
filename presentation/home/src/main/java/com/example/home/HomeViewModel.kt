package com.example.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.entity.game.rule.BoardRule
import com.example.entity.game.rule.GameRule
import com.example.entity.game.rule.Hande
import com.example.entity.game.rule.Turn
import com.example.entity.game.rule.UserRule
import com.example.entity.game.rule.UsersRule
import com.example.home.model.GameRuleSettingUiModel
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
            ruleItems = listOf(
                GameRuleSettingUiModel.Normal(),
                GameRuleSettingUiModel.FirstCheck(),
            ),
            showRuleItemIndex = 0,
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    private val mutableGameStartEffect: MutableSharedFlow<Effect.GameStart> = MutableSharedFlow()
    val gameStartEffect: SharedFlow<Effect.GameStart> = mutableGameStartEffect.asSharedFlow()

    fun changePieceHandeByNormalItem(selectedHande: GameRuleSettingUiModel.SelectedHande) {
        _uiState.value = uiState.value.copy(
            ruleItems = uiState.value.ruleItems.toMutableList().map {
                when (it) {
                    is GameRuleSettingUiModel.FirstCheck -> it
                    is GameRuleSettingUiModel.Normal -> it.copy(
                        selectedHande = selectedHande,
                    )
                }
            },
        )
    }

    fun changePieceHandeByFirstCheckItem(selectedHande: GameRuleSettingUiModel.SelectedHande) {
        _uiState.value = uiState.value.copy(
            ruleItems = uiState.value.ruleItems.toMutableList().map {
                when (it) {
                    is GameRuleSettingUiModel.FirstCheck -> it.copy(
                        selectedHande = selectedHande,
                    )

                    is GameRuleSettingUiModel.Normal -> it
                }
            },
        )
    }

    fun onGameStartClick() {
        val setting = uiState.value.ruleItems[uiState.value.showRuleItemIndex]
        val isFirstCheckEnd = when (setting) {
            is GameRuleSettingUiModel.FirstCheck -> true
            is GameRuleSettingUiModel.Normal -> false
        }
        val handeTurn = setting.selectedHande.turn
        val hande = setting.selectedHande.hande
        val blackHande = when (handeTurn) {
            Turn.Normal.Black -> hande
            Turn.Normal.White -> Hande.NON
        }
        val whiteHande = when (handeTurn) {
            Turn.Normal.Black -> Hande.NON
            Turn.Normal.White -> hande
        }
        val rule = GameRule(
            boardRule = BoardRule(),
            usersRule = UsersRule(
                blackRule = UserRule(
                    hande = blackHande,
                    isFirstCheckEnd = isFirstCheckEnd,
                ),
                whiteRule = UserRule(
                    hande = whiteHande,
                    isFirstCheckEnd = isFirstCheckEnd,
                )
            ),
        )

        useCase.setGameRule(rule)
        viewModelScope.launch {
            mutableGameStartEffect.emit(Effect.GameStart)
        }
    }

    fun changePage(pageIndex: Int) {
        _uiState.value = uiState.value.copy(
            showRuleItemIndex = pageIndex,
        )
    }

    /**
     * 画面状態
     *
     * @property ruleItems 設定カード一覧
     * @property showRuleItemIndex 表示されている設定Card
     */
    data class UiState(
        val ruleItems: List<GameRuleSettingUiModel>,
        val showRuleItemIndex: Int,
    )

    sealed interface Effect {

        /**
         * 対局開始
         */
        data object GameStart : Effect
    }
}
