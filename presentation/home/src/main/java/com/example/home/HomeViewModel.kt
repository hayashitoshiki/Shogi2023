package com.example.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domainObject.game.rule.BoardRule
import com.example.domainObject.game.rule.GameRule
import com.example.domainObject.game.rule.Hande
import com.example.domainObject.game.rule.PlayerRule
import com.example.domainObject.game.rule.PlayersRule
import com.example.domainObject.game.rule.Turn
import com.example.home.model.GameRuleSettingUiModel
import com.example.home.model.TimeLimitCardUiModel
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
                GameRuleSettingUiModel.NonCustom.Normal.INIT,
                GameRuleSettingUiModel.NonCustom.FirstCheck.INIT,
                GameRuleSettingUiModel.Custom.INIT,
            ),
            timeLimitCard = TimeLimitCardUiModel.INIT,
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
                    is GameRuleSettingUiModel.NonCustom.Normal -> it.copy(selectedHande = selectedHande)
                    is GameRuleSettingUiModel.NonCustom.FirstCheck,
                    is GameRuleSettingUiModel.Custom -> it
                }
            },
        )
    }

    fun changePieceHandeByFirstCheckItem(selectedHande: GameRuleSettingUiModel.SelectedHande) {
        _uiState.value = uiState.value.copy(
            ruleItems = uiState.value.ruleItems.toMutableList().map {
                when (it) {
                    is GameRuleSettingUiModel.NonCustom.FirstCheck -> it.copy(selectedHande = selectedHande)
                    is GameRuleSettingUiModel.NonCustom.Normal,
                    is GameRuleSettingUiModel.Custom -> it
                }
            },
        )
    }

    fun changePieceHandeByCustomItem(selectedHande: GameRuleSettingUiModel.SelectedHande) {
        _uiState.value = uiState.value.copy(
            ruleItems = uiState.value.ruleItems.toMutableList().map {
                when (it) {
                    is GameRuleSettingUiModel.Custom -> {
                        val (black, whiteRule) = when (selectedHande.turn) {
                            Turn.Normal.Black -> {
                                it.playersRule.blackRule.copy(hande = selectedHande.hande) to
                                        it.playersRule.whiteRule
                            }

                            Turn.Normal.White -> {
                                it.playersRule.blackRule to
                                        it.playersRule.whiteRule.copy(hande = selectedHande.hande)
                            }
                        }
                        val playersRule = PlayersRule(
                            blackRule = black,
                            whiteRule = whiteRule,
                        )
                        it.copy(playersRule = playersRule)
                    }

                    is GameRuleSettingUiModel.NonCustom.FirstCheck,
                    is GameRuleSettingUiModel.NonCustom.Normal -> it
                }
            },
        )
    }

    fun onChangeFirstCheck(turn: Turn, isFirstCheck: Boolean) {
        _uiState.value = uiState.value.copy(
            ruleItems = uiState.value.ruleItems.toMutableList().map {
                when (it) {
                    is GameRuleSettingUiModel.Custom -> {
                        val (black, whiteRule) = when (turn) {
                            Turn.Normal.Black -> {
                                it.playersRule.blackRule.copy(isFirstCheckEnd = isFirstCheck) to
                                        it.playersRule.whiteRule
                            }

                            Turn.Normal.White -> {
                                it.playersRule.blackRule to
                                        it.playersRule.whiteRule.copy(isFirstCheckEnd = isFirstCheck)
                            }
                        }
                        val playersRule = PlayersRule(
                            blackRule = black,
                            whiteRule = whiteRule,
                        )
                        it.copy(playersRule = playersRule)
                    }

                    is GameRuleSettingUiModel.NonCustom.FirstCheck,
                    is GameRuleSettingUiModel.NonCustom.Normal -> it
                }
            },
        )
    }

    fun onGameStartClick() {
        val playerRules =
            when (val setting = uiState.value.ruleItems[uiState.value.showRuleItemIndex]) {
                is GameRuleSettingUiModel.Custom -> setting.playersRule
                is GameRuleSettingUiModel.NonCustom -> {
                    val isFirstCheckEnd = setting is GameRuleSettingUiModel.NonCustom.FirstCheck
                    val (blackHande,whiteHande) = when(setting.selectedHande.turn) {
                        Turn.Normal.Black -> setting.selectedHande.hande to Hande.NON
                        Turn.Normal.White -> Hande.NON to setting.selectedHande.hande
                    }
                    PlayersRule(
                        blackRule = PlayerRule(
                            hande = blackHande,
                            isFirstCheckEnd = isFirstCheckEnd,
                        ),
                        whiteRule = PlayerRule(
                            hande = whiteHande,
                            isFirstCheckEnd = isFirstCheckEnd,
                        ),
                    )
                }
            }
        val gameRule = GameRule(
            boardRule = BoardRule(),
            playersRule = playerRules,
        )

        useCase.setGameRule(gameRule)
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
        val timeLimitCard: TimeLimitCardUiModel,
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
