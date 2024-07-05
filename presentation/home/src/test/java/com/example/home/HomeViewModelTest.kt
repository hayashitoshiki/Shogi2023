package com.example.home

import com.example.domainObject.game.game.Seconds
import com.example.domainObject.game.rule.BoardHandeRule
import com.example.domainObject.game.rule.BoardRule
import com.example.domainObject.game.rule.GameLogicRule
import com.example.domainObject.game.rule.GameRule
import com.example.domainObject.game.rule.GameTimeLimitRule
import com.example.domainObject.game.rule.Hande
import com.example.domainObject.game.rule.Turn
import com.example.home.model.GameRuleSettingUiModel
import com.example.home.model.TimeLimitCardUiModel
import com.example.test.core.ViewModelTest
import com.example.testDomainObject.game.fake
import com.example.testDomainObject.rule.fake
import com.example.testusecase.usecase.FakeHomeUseCase
import junit.framework.TestCase.assertEquals
import org.junit.Test

class HomeViewModelTest : ViewModelTest<HomeViewModel, HomeViewModel.UiState, HomeViewModel.Effect>() {

    private lateinit var homeUseCase: FakeHomeUseCase

    override val initUiState = HomeViewModel.UiState(
        timeLimitCard = TimeLimitCardUiModel.INIT,
        ruleItems = GameRuleSettingUiModel.fakeList(),
        showRuleItemIndex = 0,
    )

    override fun setUpUseCase() {
        homeUseCase = FakeHomeUseCase()
    }

    override fun setUpViewModel() {
        viewModel = HomeViewModel(homeUseCase)
    }

    @Test
    fun 通常モードのハンデ設定変更() {
        val selectedHande = GameRuleSettingUiModel.SelectedHande.fake(
            hande = Hande.KAKU,
            turn = Turn.Normal.Black,
        )
        val resultUiState = initUiState.copy(
            ruleItems = initUiState.ruleItems.toMutableList().map {
                when (it) {
                    is GameRuleSettingUiModel.NonCustom.Normal -> it.copy(selectedHande = selectedHande)
                    is GameRuleSettingUiModel.NonCustom.FirstCheck,
                    is GameRuleSettingUiModel.Custom,
                    -> it
                }
            },
        )

        viewModelAction(
            useCaseSet = {},
            action = {
                changePieceHandeByNormalItem(selectedHande)
            },
        )
        result(
            useCaseAsserts = emptyList(),
            state = resultUiState,
            effects = listOf(),
        )
    }

    @Test
    fun 王手将棋モードのハンデ設定変更() {
        val selectedHande = GameRuleSettingUiModel.SelectedHande.fake(
            hande = Hande.FOR,
            turn = Turn.Normal.White,
        )
        val resultUiState = initUiState.copy(
            ruleItems = initUiState.ruleItems.toMutableList().map {
                when (it) {
                    is GameRuleSettingUiModel.NonCustom.FirstCheck -> it.copy(selectedHande = selectedHande)
                    is GameRuleSettingUiModel.NonCustom.Normal,
                    is GameRuleSettingUiModel.Custom,
                    -> it
                }
            },
        )

        viewModelAction(
            useCaseSet = {},
            action = {
                changePieceHandeByFirstCheckItem(selectedHande)
            },
        )
        result(
            useCaseAsserts = emptyList(),
            state = resultUiState,
            effects = listOf(),
        )
    }

    @Test
    fun カスタム将棋モードのハンデ設定変更() {
        val selectedHande1 = GameRuleSettingUiModel.SelectedHande.fake(
            hande = Hande.FOR,
            turn = Turn.Normal.Black,
        )
        val boardHandeRule = BoardHandeRule.fake(
            blackHande = selectedHande1.hande,
        )
        val resultUiState1 = initUiState.copy(
            ruleItems = initUiState.ruleItems.toMutableList().map {
                when (it) {
                    is GameRuleSettingUiModel.NonCustom.FirstCheck,
                    is GameRuleSettingUiModel.NonCustom.Normal,
                    -> it
                    is GameRuleSettingUiModel.Custom -> it.copy(
                        boardHandeRule = boardHandeRule,
                    )
                }
            },
        )
        viewModelAction(
            useCaseSet = {},
            action = {
                changePieceHandeByCustomItem(selectedHande1)
            },
        )
        result(
            useCaseAsserts = emptyList(),
            state = resultUiState1,
            effects = listOf(),
        )

        val selectedHande2 = GameRuleSettingUiModel.SelectedHande.fake(
            hande = Hande.HISYA,
            turn = Turn.Normal.White,
        )
        val boardHandeRule2 = boardHandeRule.copy(
            whiteHande = selectedHande2.hande,
        )
        val resultUiState2 = resultUiState1.copy(
            ruleItems = resultUiState1.ruleItems.toMutableList().map {
                when (it) {
                    is GameRuleSettingUiModel.NonCustom.FirstCheck,
                    is GameRuleSettingUiModel.NonCustom.Normal,
                    -> it
                    is GameRuleSettingUiModel.Custom -> it.copy(
                        boardHandeRule = boardHandeRule2,
                    )
                }
            },
        )
        viewModelAction(
            useCaseSet = {},
            action = {
                changePieceHandeByCustomItem(selectedHande1)
                changePieceHandeByCustomItem(selectedHande2)
            },
        )
        result(
            useCaseAsserts = emptyList(),
            state = resultUiState2,
            effects = listOf(),
        )
    }

    @Test
    fun 持ち時間切れ負け設定変更_先手秒読み() {
        val seconds = Seconds.fake(600)
        val turn = Turn.Normal.Black
        val resultUiState = initUiState.copy(
            timeLimitCard = initUiState.timeLimitCard.copy(
                timeLimitRule = initUiState.timeLimitCard.timeLimitRule.copy(
                   blackTimeLimitRule = initUiState.timeLimitCard.timeLimitRule.blackTimeLimitRule.copy(
                       byoyomi = seconds,
                   ),
                ),
            ),
        )

        onChangeTimeLimitSecondTest(
            turn = turn,
            seconds = seconds,
            resultUiState = resultUiState,
        )
    }

    @Test
    fun 持ち時間切れ負け設定変更_後手秒読み() {
        val seconds = Seconds.fake(500)
        val turn = Turn.Normal.White
        val resultUiState = initUiState.copy(
            timeLimitCard = initUiState.timeLimitCard.copy(
                timeLimitRule = initUiState.timeLimitCard.timeLimitRule.copy(
                    whiteTimeLimitRule = initUiState.timeLimitCard.timeLimitRule.whiteTimeLimitRule.copy(
                        byoyomi = seconds,
                    ),
                ),
            ),
        )

        onChangeTimeLimitSecondTest(
            turn = turn,
            seconds = seconds,
            resultUiState = resultUiState,
        )
    }

    private fun onChangeTimeLimitSecondTest(
        turn: Turn,
        seconds: Seconds,
        resultUiState: HomeViewModel.UiState,
    ) {
        viewModelAction(
            useCaseSet = {},
            action = {
                onChangeTimeLimitSecond(turn, seconds)
            },
        )
        result(
            useCaseAsserts = emptyList(),
            state = resultUiState,
            effects = listOf(),
        )
    }

    @Test
    fun 持ち時間切れ負け設定変更_先手切れ負け() {
        val seconds = Seconds.fake(23500)
        val turn = Turn.Normal.Black
        val resultUiState = initUiState.copy(
            timeLimitCard = initUiState.timeLimitCard.copy(
                timeLimitRule = initUiState.timeLimitCard.timeLimitRule.copy(
                    blackTimeLimitRule = initUiState.timeLimitCard.timeLimitRule.blackTimeLimitRule.copy(
                        totalTime = seconds,
                    ),
                ),
            ),
        )

        onChangeTimeLimitTotalTimeTest(
            turn = turn,
            seconds = seconds,
            resultUiState = resultUiState,
        )
    }

    @Test
    fun 持ち時間切れ負け設定変更_後手切れ負け() {
        val seconds = Seconds.fake(23400)
        val turn = Turn.Normal.White
        val resultUiState = initUiState.copy(
            timeLimitCard = initUiState.timeLimitCard.copy(
                timeLimitRule = initUiState.timeLimitCard.timeLimitRule.copy(
                    whiteTimeLimitRule = initUiState.timeLimitCard.timeLimitRule.whiteTimeLimitRule.copy(
                        totalTime = seconds,
                    ),
                ),
            ),
        )

        onChangeTimeLimitTotalTimeTest(
            turn = turn,
            seconds = seconds,
            resultUiState = resultUiState,
        )
    }

    private fun onChangeTimeLimitTotalTimeTest(
        turn: Turn,
        seconds: Seconds,
        resultUiState: HomeViewModel.UiState,
    ) {
        viewModelAction(
            useCaseSet = {},
            action = {
                onChangeTimeLimitTotalTime(turn, seconds)
            },
        )
        result(
            useCaseAsserts = emptyList(),
            state = resultUiState,
            effects = listOf(),
        )
    }

    @Test
    fun カスタムルールで王手将棋の設定をする_先手() {
        val turn = Turn.Normal.Black
        val isFirstCheck1 = true
        val resultUiState1 = initUiState.copy(
            ruleItems = initUiState.ruleItems.toMutableList().map {
                when (it) {
                    is GameRuleSettingUiModel.NonCustom.FirstCheck,
                    is GameRuleSettingUiModel.NonCustom.Normal,
                    -> it
                    is GameRuleSettingUiModel.Custom -> it.copy(
                        logicRule = it.logicRule.copy(
                            firstCheckEnd = it.logicRule.firstCheckEnd.copy(
                                blackRule = isFirstCheck1,
                            ),
                        ),
                    )
                }
            },
        )

        onChangeFirstCheckTest(
            turn = turn,
            isFirstCheck1 = isFirstCheck1,
            resultUiState1 = resultUiState1,
        )

        val isFirstCheck2 = false
        val resultUiState2 = resultUiState1.copy(
            ruleItems = resultUiState1.ruleItems.toMutableList().map {
                when (it) {
                    is GameRuleSettingUiModel.NonCustom.FirstCheck,
                    is GameRuleSettingUiModel.NonCustom.Normal,
                    -> it
                    is GameRuleSettingUiModel.Custom -> it.copy(
                        logicRule = it.logicRule.copy(
                            firstCheckEnd = it.logicRule.firstCheckEnd.copy(
                                blackRule = isFirstCheck2,
                            ),
                        ),
                    )
                }
            },
        )
        onChangeFirstCheckTest(
            turn = turn,
            isFirstCheck1 = isFirstCheck2,
            resultUiState1 = resultUiState2,
        )
    }

    @Test
    fun カスタムルールで王手将棋の設定をする_後手() {
        val turn = Turn.Normal.White
        val isFirstCheck1 = true
        val resultUiState1 = initUiState.copy(
            ruleItems = initUiState.ruleItems.toMutableList().map {
                when (it) {
                    is GameRuleSettingUiModel.NonCustom.FirstCheck,
                    is GameRuleSettingUiModel.NonCustom.Normal,
                    -> it
                    is GameRuleSettingUiModel.Custom -> it.copy(
                        logicRule = it.logicRule.copy(
                            firstCheckEnd = it.logicRule.firstCheckEnd.copy(
                                whiteRule = isFirstCheck1,
                            ),
                        ),
                    )
                }
            },
        )

        onChangeFirstCheckTest(
            turn = turn,
            isFirstCheck1 = isFirstCheck1,
            resultUiState1 = resultUiState1,
        )

        val isFirstCheck2 = false
        val resultUiStat2 = resultUiState1.copy(
            ruleItems = resultUiState1.ruleItems.toMutableList().map {
                when (it) {
                    is GameRuleSettingUiModel.NonCustom.FirstCheck,
                    is GameRuleSettingUiModel.NonCustom.Normal,
                    -> it
                    is GameRuleSettingUiModel.Custom -> it.copy(
                        logicRule = it.logicRule.copy(
                            firstCheckEnd = it.logicRule.firstCheckEnd.copy(
                                whiteRule = isFirstCheck2,
                            ),
                        ),
                    )
                }
            },
        )
        onChangeFirstCheckTest(
            turn = turn,
            isFirstCheck1 = isFirstCheck2,
            resultUiState1 = resultUiStat2,
        )
    }

    private fun onChangeFirstCheckTest(
        turn: Turn,
        isFirstCheck1: Boolean,
        resultUiState1: HomeViewModel.UiState,
    ) {
        viewModelAction(
            useCaseSet = {},
            action = {
                onChangeFirstCheck(turn, isFirstCheck1)
            },
        )
        result(
            useCaseAsserts = emptyList(),
            state = resultUiState1,
            effects = listOf(),
        )
    }

    @Test
    fun 設定ページ変更() {
        val pageIndex = 1
        val resultUiState = initUiState.copy(
            showRuleItemIndex = pageIndex,
        )

        viewModelAction(
            useCaseSet = {},
            action = {
                changePage(pageIndex)
            },
        )
        result(
            useCaseAsserts = emptyList(),
            state = resultUiState,
            effects = listOf(),
        )
    }

    @Test
    fun 将棋開始_普通の将棋_１ページ目() {
        val pageIndex = 0
        val resultUiState = initUiState.copy(
            showRuleItemIndex = pageIndex,
        )
        val timeLimitCard = initUiState.ruleItems[pageIndex] as GameRuleSettingUiModel.NonCustom.Normal
        val blackHande: Hande
        val whiteHande: Hande
        when (timeLimitCard.selectedHande.turn) {
            Turn.Normal.Black -> {
                blackHande = timeLimitCard.selectedHande.hande
                whiteHande = Hande.NON
            }
            Turn.Normal.White -> {
                blackHande = Hande.NON
                whiteHande = timeLimitCard.selectedHande.hande
            }
        }
        val resultGameRule = GameRule(
            boardRule = BoardRule.fake(
                setUpRule = BoardRule.SetUpRule.NORMAL,
                boardHandeRule = BoardHandeRule.fake(
                    blackHande = blackHande,
                    whiteHande = whiteHande,
                )
            ),
            timeLimitRule = GameTimeLimitRule.fake(
                blackTimeLimitRule = initUiState.timeLimitCard.timeLimitRule.blackTimeLimitRule,
                whiteTimeLimitRule = initUiState.timeLimitCard.timeLimitRule.whiteTimeLimitRule,
            ),
            logicRule = GameLogicRule.fake(),
        )

        onGameStartClickTest(
            pageIndex = pageIndex,
            resultGameRule = resultGameRule,
            resultUiState = resultUiState,
        )
    }

    @Test
    fun 将棋開始_王手将棋_２ページ目() {
        val pageIndex = 1
        val resultUiState = initUiState.copy(
            showRuleItemIndex = pageIndex,
        )
        val timeLimitCard = initUiState.ruleItems[pageIndex] as GameRuleSettingUiModel.NonCustom.FirstCheck
        val blackHande: Hande
        val whiteHande: Hande
        when (timeLimitCard.selectedHande.turn) {
            Turn.Normal.Black -> {
                blackHande = timeLimitCard.selectedHande.hande
                whiteHande = Hande.NON
            }
            Turn.Normal.White -> {
                blackHande = Hande.NON
                whiteHande = timeLimitCard.selectedHande.hande
            }
        }
        val resultGameRule = GameRule(
            boardRule = BoardRule(
                setUpRule = BoardRule.SetUpRule.NORMAL,
                boardHandeRule = BoardHandeRule.fake(
                    blackHande = blackHande,
                    whiteHande = whiteHande,
                )
            ),
            timeLimitRule = GameTimeLimitRule.fake(
                blackTimeLimitRule = initUiState.timeLimitCard.timeLimitRule.blackTimeLimitRule,
                whiteTimeLimitRule = initUiState.timeLimitCard.timeLimitRule.whiteTimeLimitRule,
            ),
            logicRule = GameLogicRule.fake(
                firstCheckEnd = GameLogicRule.Rule.FirstCheckEndRule.fake(
                    blackRule = true,
                    whiteRule = true,
                ),
            ),
        )

        onGameStartClickTest(
            pageIndex = pageIndex,
            resultGameRule = resultGameRule,
            resultUiState = resultUiState,
        )
    }

    @Test
    fun 将棋開始_カスタム将棋_３ページ目() {
        val pageIndex = 2
        val resultUiState = initUiState.copy(
            showRuleItemIndex = pageIndex,
        )
        val timeLimitCard = initUiState.ruleItems[pageIndex] as GameRuleSettingUiModel.Custom
        val resultGameRule = GameRule(
            boardRule = BoardRule(
                setUpRule = BoardRule.SetUpRule.NORMAL,
            ),
            timeLimitRule = GameTimeLimitRule.fake(
                blackTimeLimitRule = initUiState.timeLimitCard.timeLimitRule.blackTimeLimitRule,
                whiteTimeLimitRule = initUiState.timeLimitCard.timeLimitRule.whiteTimeLimitRule,
            ),
            logicRule = GameLogicRule.fake(
                firstCheckEnd = GameLogicRule.Rule.FirstCheckEndRule.fake(
                    blackRule = timeLimitCard.logicRule.firstCheckEnd.blackRule,
                    whiteRule = timeLimitCard.logicRule.firstCheckEnd.whiteRule,
                ),
            ),
        )

        onGameStartClickTest(
            pageIndex = pageIndex,
            resultGameRule = resultGameRule,
            resultUiState = resultUiState,
        )
    }

    private fun onGameStartClickTest(
        pageIndex: Int,
        resultGameRule: GameRule,
        resultUiState: HomeViewModel.UiState,
    ) {
        viewModelAction(
            useCaseSet = {
                homeUseCase.setGameRuleLogic = {
                    assertEquals(it, resultGameRule)
                }
            },
            action = {
                changePage(pageIndex)
                onGameStartClick()
            },
        )
        result(
            useCaseAsserts = listOf(
                UseCaseAsserts(homeUseCase.calSsetGameRuleCount, 1),
            ),
            state = resultUiState,
            effects = listOf(),
        )
    }
}
