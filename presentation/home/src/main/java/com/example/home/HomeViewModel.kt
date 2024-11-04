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
import com.example.core.uilogic.BaseContract
import com.example.core.uilogic.BaseViewModel
import com.example.domainObject.game.game.TimeLimit
import com.example.usecaseinterface.usecase.GameSettingUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val useCase: GameSettingUseCase,
) : BaseViewModel<HomeViewModel.UiState, HomeViewModel.Effect, HomeViewModel.Action>() {

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

    override fun callAction(action: Action) {
        when(action) {
            is Action.ClickPieceHandeButtonByNormalItem -> changePieceHandeByNormalItem(action.selectedHande)
            is Action.ClickPieceHandeButtonByFirstCheckItem -> changePieceHandeByFirstCheckItem(action.selectedHande)
            is Action.ClickPieceHandeButtonByCustomItem -> changePieceHandeByCustomItem(action.selectedHande)
            is Action.ClickFirstCheckButton -> onChangeFirstCheck(action.turn, action.isFirstCheck)
            is Action.SelectTimeLimitTotalTimeDropdown -> onChangeTimeLimitTotalTime(action.turn, action.seconds)
            is Action.SelectTimeLimitSecondDropdown -> onChangeTimeLimitSecond(action.turn, action.seconds)
            is Action.ScrollRuleSettingCards -> changePage(action.pageIndex)
            Action.ClickGameStartButton -> onGameStartClick()
        }
    }
    private fun changePieceHandeByNormalItem(selectedHande: GameRuleSettingUiModel.SelectedHande) {
        changePieceHandeBy(selectedHande, SelectMode.Normal)
    }

    private fun changePieceHandeByFirstCheckItem(selectedHande: GameRuleSettingUiModel.SelectedHande) {
        changePieceHandeBy(selectedHande, SelectMode.FirstCheck)
    }

    private fun changePieceHandeByCustomItem(selectedHande: GameRuleSettingUiModel.SelectedHande) {
        changePieceHandeBy(selectedHande, SelectMode.Custom)
    }

    private fun changePieceHandeBy(selectedHande: GameRuleSettingUiModel.SelectedHande, selectMode: SelectMode) {
        setState {
            copy(
                ruleItems = ruleItems.toMutableList().map {
                    when (it) {
                        is GameRuleSettingUiModel.Custom ->
                            if (selectMode == SelectMode.Custom) {
                                val boardHandeRule = when (selectedHande.turn) {
                                    Turn.Normal.Black -> it.boardHandeRule.copy(blackHande = selectedHande.hande)
                                    Turn.Normal.White -> it.boardHandeRule.copy(whiteHande = selectedHande.hande)
                                }
                                it.copy(boardHandeRule = boardHandeRule)
                            } else it

                        is GameRuleSettingUiModel.NonCustom.FirstCheck ->
                            if (selectMode == SelectMode.FirstCheck) it.copy(selectedHande = selectedHande) else it
                        is GameRuleSettingUiModel.NonCustom.Normal ->
                            if (selectMode == SelectMode.Normal)  it.copy(selectedHande = selectedHande)else it
                    }
                },
            )
        }
    }

    private fun onChangeFirstCheck(turn: Turn, isFirstCheck: Boolean) {
        setState {
            copy(
                ruleItems = ruleItems.toMutableList().map {
                    when (it) {
                        is GameRuleSettingUiModel.Custom -> {
                            val firstCheckEnd = when (turn) {
                                Turn.Normal.Black -> it.logicRule.firstCheckEnd.copy(blackRule = isFirstCheck)
                                Turn.Normal.White -> it.logicRule.firstCheckEnd.copy(whiteRule = isFirstCheck)
                            }
                            it.copy(logicRule = it.logicRule.copy(firstCheckEnd = firstCheckEnd))
                        }

                        is GameRuleSettingUiModel.NonCustom.FirstCheck,
                        is GameRuleSettingUiModel.NonCustom.Normal,
                        -> it
                    }
                },
            )
        }
    }

    private fun onChangeTimeLimitTotalTime(turn: Turn, seconds: Seconds) {
        updateTimeLimitRule(turn) { it.copy(totalTime = seconds) }
    }

    private fun onChangeTimeLimitSecond(turn: Turn, seconds: Seconds) {
        updateTimeLimitRule(turn) { it.copy(byoyomi = seconds) }
    }

    private fun updateTimeLimitRule(turn: Turn, update: (PlayerTimeLimitRule) -> PlayerTimeLimitRule) {
        setState {
            copy(
                timeLimitCard = timeLimitCard.copy(
                    timeLimitRule = state.value.timeLimitCard.timeLimitRule.let {
                        when (turn) {
                            Turn.Normal.Black -> it.copy(blackTimeLimitRule = update(it.blackTimeLimitRule))
                            Turn.Normal.White -> it.copy(whiteTimeLimitRule = update(it.whiteTimeLimitRule))
                        }
                    },
                ),
            )
        }
    }

    private fun onGameStartClick() {
        val logicRule: GameLogicRule
        val boardRule: BoardRule
        when (val setting = state.value.ruleItems[state.value.showRuleItemIndex]) {
            is GameRuleSettingUiModel.Custom -> {
                logicRule = setting.logicRule
                boardRule = BoardRule(boardHandeRule = setting.boardHandeRule)
            }
            is GameRuleSettingUiModel.NonCustom -> {
                val (blackHande, whiteHande) = when (setting.selectedHande.turn) {
                    Turn.Normal.Black -> setting.selectedHande.hande to Hande.NON
                    Turn.Normal.White -> Hande.NON to setting.selectedHande.hande
                }
                boardRule = BoardRule(
                    boardHandeRule = BoardHandeRule(
                        blackHande = blackHande,
                        whiteHande = whiteHande,
                    ),
                )
                logicRule = when(setting) {
                    is GameRuleSettingUiModel.NonCustom.Normal -> GameLogicRule.DEFAULT
                    is GameRuleSettingUiModel.NonCustom.FirstCheck -> {
                        GameLogicRule(
                            tryRule = GameLogicRule.Rule.TryRule.DEFAULT,
                            firstCheckEnd = GameLogicRule.Rule.FirstCheckEndRule.set(true),
                        )
                    }
                }
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

    private fun changePage(pageIndex: Int) {
        setState {
            copy(
                showRuleItemIndex = pageIndex,
            )
        }
    }

    enum class SelectMode {
        FirstCheck,
        Normal,
        Custom,
    }

    /**
     * 画面状態
     *
     * @property timeLimitCard 持ち時間設定カード
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

    sealed interface Action: BaseContract.Action {
        data class ClickPieceHandeButtonByNormalItem(val selectedHande: GameRuleSettingUiModel.SelectedHande): Action
        data class ClickPieceHandeButtonByFirstCheckItem(val selectedHande: GameRuleSettingUiModel.SelectedHande): Action
        data class ClickPieceHandeButtonByCustomItem(val selectedHande: GameRuleSettingUiModel.SelectedHande): Action
        data class ClickFirstCheckButton(val turn: Turn, val isFirstCheck: Boolean): Action
        data class SelectTimeLimitTotalTimeDropdown(val turn: Turn, val seconds: Seconds): Action
        data class SelectTimeLimitSecondDropdown(val turn: Turn, val seconds: Seconds): Action
        data class ScrollRuleSettingCards(val pageIndex: Int): Action
        data object ClickGameStartButton: Action
    }
}
