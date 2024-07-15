package com.example.home

import androidx.lifecycle.viewModelScope
import com.example.domainObject.game.game.Seconds
import com.example.domainObject.game.rule.BoardHandeRule
import com.example.domainObject.game.rule.BoardRule
import com.example.domainObject.game.rule.GameLogicRule
import com.example.domainObject.game.rule.GameRule
import com.example.domainObject.game.rule.Hande
import com.example.domainObject.game.rule.PlayerTimeLimitRule
import com.example.domainObject.game.rule.Turn
import com.example.home.model.GameRuleSettingUiModel
import com.example.home.model.TimeLimitCardUiModel
import com.example.test.uilogic.BaseContract
import com.example.test.uilogic.BaseViewModel
import com.example.usecaseinterface.usecase.HomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: HomeUseCase,
) : BaseViewModel<HomeViewModel.UiState, HomeViewModel.Effect>() {

    init {
        viewModelScope.launch {
            val timeLimitCardUiModel = TimeLimitCardUiModel(useCase.getTimeLimits())
            setState {
                copy(
                    timeLimitCard = timeLimitCardUiModel,
                )
            }
        }
    }

    override fun initState(): UiState {
        return UiState(
            ruleItems = listOf(
                GameRuleSettingUiModel.NonCustom.Normal.INIT,
                GameRuleSettingUiModel.NonCustom.FirstCheck.INIT,
                GameRuleSettingUiModel.Custom.INIT,
            ),
            timeLimitCard = TimeLimitCardUiModel.INIT,
            showRuleItemIndex = 0,
        )
    }

    fun changePieceHandeByNormalItem(selectedHande: GameRuleSettingUiModel.SelectedHande) {
        setState {
            copy(
                ruleItems = ruleItems.toMutableList().map {
                    when (it) {
                        is GameRuleSettingUiModel.NonCustom.Normal -> it.copy(selectedHande = selectedHande)
                        is GameRuleSettingUiModel.NonCustom.FirstCheck,
                        is GameRuleSettingUiModel.Custom,
                        -> it
                    }
                },
            )
        }
    }

    fun changePieceHandeByFirstCheckItem(selectedHande: GameRuleSettingUiModel.SelectedHande) {
        setState {
            copy(
                ruleItems = ruleItems.toMutableList().map {
                    when (it) {
                        is GameRuleSettingUiModel.NonCustom.FirstCheck -> it.copy(selectedHande = selectedHande)
                        is GameRuleSettingUiModel.NonCustom.Normal,
                        is GameRuleSettingUiModel.Custom,
                        -> it
                    }
                },
            )
        }
    }

    fun changePieceHandeByCustomItem(selectedHande: GameRuleSettingUiModel.SelectedHande) {
        setState {
            copy(
                ruleItems = ruleItems.toMutableList().map {
                    when (it) {
                        is GameRuleSettingUiModel.Custom -> {
                            val boardHandeRule = when (selectedHande.turn) {
                                Turn.Normal.Black -> {
                                    it.boardHandeRule.copy(
                                        blackHande = selectedHande.hande,
                                    )
                                }

                                Turn.Normal.White -> {
                                    it.boardHandeRule.copy(
                                        whiteHande = selectedHande.hande,
                                    )
                                }
                            }
                            it.copy(boardHandeRule = boardHandeRule)
                        }

                        is GameRuleSettingUiModel.NonCustom.FirstCheck,
                        is GameRuleSettingUiModel.NonCustom.Normal,
                        -> it
                    }
                },
            )
        }
    }

    fun onChangeFirstCheck(turn: Turn, isFirstCheck: Boolean) {
        setState {
            copy(
                ruleItems = ruleItems.toMutableList().map {
                    when (it) {
                        is GameRuleSettingUiModel.Custom -> {
                            val firstCheckEnd = when (turn) {
                                Turn.Normal.Black -> {
                                    it.logicRule.firstCheckEnd.copy(
                                        blackRule = isFirstCheck,
                                    )
                                }

                                Turn.Normal.White -> {
                                    it.logicRule.firstCheckEnd.copy(
                                        whiteRule = isFirstCheck,
                                    )
                                }
                            }
                            it.copy(
                                logicRule = it.logicRule.copy(
                                    firstCheckEnd = firstCheckEnd,
                                ),
                            )
                        }

                        is GameRuleSettingUiModel.NonCustom.FirstCheck,
                        is GameRuleSettingUiModel.NonCustom.Normal,
                        -> it
                    }
                },
            )
        }
    }

    fun onChangeTimeLimitTotalTime(turn: Turn, seconds: Seconds) {
        val byoyomi = getTimeLimitRuleByTurn(turn).byoyomi
        updateTimeLimitRule(turn, seconds, byoyomi)
    }

    fun onChangeTimeLimitSecond(turn: Turn, seconds: Seconds) {
        val totalTime = getTimeLimitRuleByTurn(turn).totalTime
        updateTimeLimitRule(turn, totalTime, seconds)
    }

    private fun getTimeLimitRuleByTurn(turn: Turn): PlayerTimeLimitRule {
        val timeLimitRule = state.value.timeLimitCard.timeLimitRule
        return when (turn) {
            Turn.Normal.Black -> timeLimitRule.blackTimeLimitRule
            Turn.Normal.White -> timeLimitRule.whiteTimeLimitRule
        }
    }

    private fun updateTimeLimitRule(turn: Turn, totalTime: Seconds, byoyomi: Seconds) {
        val timeLimitRule = when (turn) {
            Turn.Normal.Black -> {
                state.value.timeLimitCard.timeLimitRule.copy(
                    blackTimeLimitRule = state.value.timeLimitCard.timeLimitRule.blackTimeLimitRule.copy(
                        totalTime = totalTime,
                        byoyomi = byoyomi,
                    ),
                )
            }
            Turn.Normal.White -> {
                state.value.timeLimitCard.timeLimitRule.copy(
                    whiteTimeLimitRule = state.value.timeLimitCard.timeLimitRule.whiteTimeLimitRule.copy(
                        totalTime = totalTime,
                        byoyomi = byoyomi,
                    ),
                )
            }
        }
        setState {
            copy(
                timeLimitCard = timeLimitCard.copy(
                    timeLimitRule = timeLimitRule,
                ),
            )
        }
    }

    fun onGameStartClick() {
        val logicRule: GameLogicRule
        val boardRule: BoardRule
        when (val setting = state.value.ruleItems[state.value.showRuleItemIndex]) {
            is GameRuleSettingUiModel.Custom -> {
                logicRule = setting.logicRule
                boardRule = BoardRule(boardHandeRule = setting.boardHandeRule)
            }
            is GameRuleSettingUiModel.NonCustom.Normal -> {
                val (blackHande, whiteHande) = when (setting.selectedHande.turn) {
                    Turn.Normal.Black -> setting.selectedHande.hande to Hande.NON
                    Turn.Normal.White -> Hande.NON to setting.selectedHande.hande
                }
                logicRule = GameLogicRule.DEFAULT
                boardRule = BoardRule(
                    boardHandeRule = BoardHandeRule(
                        blackHande = blackHande,
                        whiteHande = whiteHande,
                    ),
                )
            }
            is GameRuleSettingUiModel.NonCustom -> {
                val (blackHande, whiteHande) = when (setting.selectedHande.turn) {
                    Turn.Normal.Black -> setting.selectedHande.hande to Hande.NON
                    Turn.Normal.White -> Hande.NON to setting.selectedHande.hande
                }
                logicRule = GameLogicRule(
                    tryRule = GameLogicRule.Rule.TryRule.DEFAULT,
                    firstCheckEnd = GameLogicRule.Rule.FirstCheckEndRule.set(true),
                )
                boardRule = BoardRule(
                    boardHandeRule = BoardHandeRule(
                        blackHande = blackHande,
                        whiteHande = whiteHande,
                    ),
                )
            }
        }
        val timeLimitRule = state.value.timeLimitCard.timeLimitRule
        val gameRule = GameRule(
            boardRule = boardRule,
            logicRule = logicRule,
            timeLimitRule = timeLimitRule,
        )

        viewModelScope.launch {
            async { useCase.setGameRule(gameRule) }.await()
            setEffect { Effect.GameStart }
        }
    }

    fun changePage(pageIndex: Int) {
        setState {
            copy(
                showRuleItemIndex = pageIndex,
            )
        }
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
    ) : BaseContract.State

    sealed interface Effect : BaseContract.Effect {

        /**
         * 対局開始
         */
        data object GameStart : Effect
    }
}
