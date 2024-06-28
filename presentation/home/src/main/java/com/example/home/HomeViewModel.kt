package com.example.home

import com.example.core.uilogic.BaseContract
import com.example.core.uilogic.BaseViewModel
import com.example.domainObject.game.game.Second
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
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: HomeUseCase,
) : BaseViewModel<HomeViewModel.UiState, HomeViewModel.Effect>() {

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
                        is GameRuleSettingUiModel.Custom -> it
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
                        is GameRuleSettingUiModel.Custom -> it
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
    }

    fun onChangeFirstCheck(turn: Turn, isFirstCheck: Boolean) {
        setState {
            copy(
                ruleItems = ruleItems.toMutableList().map {
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
    }

    fun onChangeTimeLimitTotalTime(turn: Turn, second: Second) {
        val byoyomi = when(turn) {
            Turn.Normal.Black -> {
                state.value.timeLimitCard.blackTimeLimitRule.byoyomi
            }
            Turn.Normal.White -> {
                state.value.timeLimitCard.whiteTimeLimitRule.byoyomi
            }
        }
        updateTimeLimitRule(turn, second, byoyomi)
    }

    fun onChangeTimeLimitSecond(turn: Turn, second: Second) {
        val totalTime = when(turn) {
            Turn.Normal.Black -> {
                state.value.timeLimitCard.blackTimeLimitRule.totalTime
            }
            Turn.Normal.White -> {
                state.value.timeLimitCard.whiteTimeLimitRule.totalTime
            }
        }
        updateTimeLimitRule(turn, totalTime, second)
    }

    private fun updateTimeLimitRule(turn: Turn, totalTime: Second, second: Second) {
        setState {
            when(turn) {
                Turn.Normal.Black -> {
                    copy(
                        timeLimitCard = timeLimitCard.copy(
                            blackTimeLimitRule = timeLimitCard.blackTimeLimitRule.copy(
                                totalTime = totalTime,
                                byoyomi = second,
                            ),
                        )
                    )
                }
                Turn.Normal.White -> {
                    copy(
                        timeLimitCard = timeLimitCard.copy(
                            whiteTimeLimitRule = timeLimitCard.whiteTimeLimitRule.copy(
                                totalTime = totalTime,
                                byoyomi = second,
                            ),
                        )
                    )
                }
            }
        }
    }

    fun onGameStartClick() {
        val playerRules =
            when (val setting = state.value.ruleItems[state.value.showRuleItemIndex]) {
                is GameRuleSettingUiModel.Custom -> {
                    setting.playersRule.copy(
                        blackRule = setting.playersRule.blackRule.copy(
                            timeLimitRule = state.value.timeLimitCard.blackTimeLimitRule,
                        ),
                        whiteRule = setting.playersRule.whiteRule.copy(
                            timeLimitRule = state.value.timeLimitCard.whiteTimeLimitRule,
                        ),
                    )
                }
                is GameRuleSettingUiModel.NonCustom -> {
                    val isFirstCheckEnd = setting is GameRuleSettingUiModel.NonCustom.FirstCheck
                    val (blackHande,whiteHande) = when(setting.selectedHande.turn) {
                        Turn.Normal.Black -> setting.selectedHande.hande to Hande.NON
                        Turn.Normal.White -> Hande.NON to setting.selectedHande.hande
                    }
                    PlayersRule(
                        blackRule = PlayerRule(
                            hande = blackHande,
                            timeLimitRule = state.value.timeLimitCard.blackTimeLimitRule,
                            isFirstCheckEnd = isFirstCheckEnd,
                        ),
                        whiteRule = PlayerRule(
                            hande = whiteHande,
                            timeLimitRule = state.value.timeLimitCard.whiteTimeLimitRule,
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
        setEffect { Effect.GameStart }
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
    ): BaseContract.State

    sealed interface Effect : BaseContract.Effect {

        /**
         * 対局開始
         */
        data object GameStart : Effect
    }
}
