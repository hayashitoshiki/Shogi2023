package com.example.home

import app.cash.turbine.test
import com.example.domainObject.game.game.Second
import com.example.domainObject.game.rule.BoardRule
import com.example.domainObject.game.rule.GameRule
import com.example.domainObject.game.rule.Hande
import com.example.domainObject.game.rule.PlayerRule
import com.example.domainObject.game.rule.PlayersRule
import com.example.domainObject.game.rule.Turn
import com.example.home.model.GameRuleSettingUiModel
import com.example.home.model.TimeLimitCardUiModel
import com.example.testDomainObject.game.fake
import com.example.testDomainObject.rule.fake
import com.example.test_usecase.usecase.FakeHomeUseCase
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class HomeViewModelTest {

  private lateinit var viewModel: HomeViewModel
  private lateinit var homeUseCase: FakeHomeUseCase
  private val initUiState = HomeViewModel.UiState(
    timeLimitCard = TimeLimitCardUiModel.INIT,
    ruleItems = GameRuleSettingUiModel.fakeList(),
    showRuleItemIndex = 0,
  )

  @OptIn(ExperimentalCoroutinesApi::class)
  @Before
  fun setup() {
    Dispatchers.setMain(StandardTestDispatcher())
    homeUseCase = FakeHomeUseCase()
  }

  @OptIn(ExperimentalCoroutinesApi::class)
  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  private fun initViewModel() {
    viewModel = HomeViewModel(homeUseCase)
  }

  /**
   * 実行結果比較
   *
   * @param state Stateの期待値
   * @param effects Effectの期待値
   */
  private fun uiResult(state: HomeViewModel.UiState, effects: List<HomeViewModel.Effect>)  = runTest {
    val resultState = viewModel.uiState.value

    // 比較
    Assert.assertEquals(resultState, state)
    // Effect
    effects.forEach { effect ->
      viewModel.gameStartEffect.test {
        val item = awaitItem()
        Assert.assertEquals(effect, item)
      }
    }

  }

  @Test
  fun 通常モードのハンデ設定変更() {
    initViewModel()

    val selectedHande = GameRuleSettingUiModel.SelectedHande.fake(
      hande = Hande.KAKU,
      turn = Turn.Normal.Black,
    )
    val resultUiState = initUiState.copy(
      ruleItems = initUiState.ruleItems.toMutableList().map {
        when (it) {
          is GameRuleSettingUiModel.NonCustom.Normal -> it.copy(selectedHande = selectedHande)
          is GameRuleSettingUiModel.NonCustom.FirstCheck,
          is GameRuleSettingUiModel.Custom -> it
        }
      }
    )
    viewModel.changePieceHandeByNormalItem(selectedHande)
    uiResult(
      state = resultUiState,
      effects = listOf()
    )
  }

  @Test
  fun 王手将棋モードのハンデ設定変更() {
    initViewModel()

    val selectedHande = GameRuleSettingUiModel.SelectedHande.fake(
      hande = Hande.FOR,
      turn = Turn.Normal.White,
    )
    val resultUiState = initUiState.copy(
      ruleItems = initUiState.ruleItems.toMutableList().map {
        when (it) {
          is GameRuleSettingUiModel.NonCustom.FirstCheck -> it.copy(selectedHande = selectedHande)
          is GameRuleSettingUiModel.NonCustom.Normal,
          is GameRuleSettingUiModel.Custom -> it
        }
      }
    )
    viewModel.changePieceHandeByFirstCheckItem(selectedHande)
    uiResult(
      state = resultUiState,
      effects = listOf()
    )
  }

  @Test
  fun カスタム将棋モードのハンデ設定変更() {
    initViewModel()

    val selectedHande1 = GameRuleSettingUiModel.SelectedHande.fake(
      hande = Hande.FOR,
      turn = Turn.Normal.Black,
    )
    val playersRule = PlayersRule.fake(
      blackHande = selectedHande1.hande,
    )
    val resultUiState1 = initUiState.copy(
      ruleItems = initUiState.ruleItems.toMutableList().map {
        when (it) {
          is GameRuleSettingUiModel.NonCustom.FirstCheck,
          is GameRuleSettingUiModel.NonCustom.Normal -> it
          is GameRuleSettingUiModel.Custom -> it.copy(playersRule = playersRule)
        }
      }
    )
    viewModel.changePieceHandeByCustomItem(selectedHande1)
    uiResult(
      state = resultUiState1,
      effects = listOf()
    )

    val selectedHande2 = GameRuleSettingUiModel.SelectedHande.fake(
      hande = Hande.HISYA,
      turn = Turn.Normal.White,
    )
    val playersRule2 = playersRule.copy(
      whiteRule = playersRule.whiteRule.copy(hande = selectedHande2.hande),
    )
    val resultUiState2 = resultUiState1.copy(
      ruleItems = resultUiState1.ruleItems.toMutableList().map {
        when (it) {
          is GameRuleSettingUiModel.NonCustom.FirstCheck,
          is GameRuleSettingUiModel.NonCustom.Normal -> it
          is GameRuleSettingUiModel.Custom -> it.copy(playersRule = playersRule2)
        }
      }
    )
    viewModel.changePieceHandeByCustomItem(selectedHande2)
    uiResult(
      state = resultUiState2,
      effects = listOf()
    )
  }

  @Test
  fun 持ち時間切れ負け設定変更_先手秒読み() {
    initViewModel()

    val second = Second.fake(600)
    val turn = Turn.Normal.Black
    val resultUiState = initUiState.copy(
      timeLimitCard = initUiState.timeLimitCard.copy(
        blackTimeLimitRule = initUiState.timeLimitCard.blackTimeLimitRule.copy(
          byoyomi = second,
        )
      )
    )
    viewModel.onChangeTimeLimitSecond(turn, second)
    uiResult(
      state = resultUiState,
      effects = listOf()
    )
  }

  @Test
  fun 持ち時間切れ負け設定変更_後手秒読み() {
    initViewModel()

    val second = Second.fake(500)
    val turn = Turn.Normal.White
    val resultUiState = initUiState.copy(
      timeLimitCard = initUiState.timeLimitCard.copy(
        whiteTimeLimitRule = initUiState.timeLimitCard.whiteTimeLimitRule.copy(
          byoyomi = second,
        )
      )
    )
    viewModel.onChangeTimeLimitSecond(turn, second)
    uiResult(
      state = resultUiState,
      effects = listOf()
    )
  }

  @Test
  fun 持ち時間切れ負け設定変更_先手切れ負け() {
    initViewModel()

    val second = Second.fake(23500)
    val turn = Turn.Normal.Black
    val resultUiState = initUiState.copy(
      timeLimitCard = initUiState.timeLimitCard.copy(
        blackTimeLimitRule = initUiState.timeLimitCard.blackTimeLimitRule.copy(
          totalTime = second,
        )
      )
    )
    viewModel.onChangeTimeLimitTotalTime(turn, second)
    uiResult(
      state = resultUiState,
      effects = listOf()
    )
  }

  @Test
  fun 持ち時間切れ負け設定変更_後手切れ負け() {
    initViewModel()

    val second = Second.fake(23400)
    val turn = Turn.Normal.White
    val resultUiState = initUiState.copy(
      timeLimitCard = initUiState.timeLimitCard.copy(
        whiteTimeLimitRule = initUiState.timeLimitCard.whiteTimeLimitRule.copy(
          totalTime = second,
        )
      )
    )
    viewModel.onChangeTimeLimitTotalTime(turn, second)
    uiResult(
      state = resultUiState,
      effects = listOf()
    )
  }

  @Test
  fun カスタムルールで王手将棋の設定をする_先手() {
    initViewModel()

    val turn = Turn.Normal.Black
    val isFirstCheck = true
    val resultUiState1 = initUiState.copy(
      ruleItems = initUiState.ruleItems.toMutableList().map {
        when (it) {
          is GameRuleSettingUiModel.NonCustom.FirstCheck,
          is GameRuleSettingUiModel.NonCustom.Normal -> it
          is GameRuleSettingUiModel.Custom -> it.copy(
            playersRule = it.playersRule.copy(
              blackRule = it.playersRule.blackRule.copy(isFirstCheckEnd = isFirstCheck)
            )
          )
        }
      }
    )
    viewModel.onChangeFirstCheck(turn, isFirstCheck)
    uiResult(
      state = resultUiState1,
      effects = listOf()
    )
    val isFirstCheck2 = false
    val resultUiStat2 = resultUiState1.copy(
      ruleItems = resultUiState1.ruleItems.toMutableList().map {
        when (it) {
          is GameRuleSettingUiModel.NonCustom.FirstCheck,
          is GameRuleSettingUiModel.NonCustom.Normal -> it
          is GameRuleSettingUiModel.Custom -> it.copy(
            playersRule = it.playersRule.copy(
              blackRule = it.playersRule.blackRule.copy(isFirstCheckEnd = isFirstCheck2)
            )
          )
        }
      }
    )
    viewModel.onChangeFirstCheck(turn, isFirstCheck2)
    uiResult(
      state = resultUiStat2,
      effects = listOf()
    )
  }

  @Test
  fun カスタムルールで王手将棋の設定をする_後手() {
    initViewModel()

    val turn = Turn.Normal.White
    val isFirstCheck = true
    val resultUiState1 = initUiState.copy(
      ruleItems = initUiState.ruleItems.toMutableList().map {
        when (it) {
          is GameRuleSettingUiModel.NonCustom.FirstCheck,
          is GameRuleSettingUiModel.NonCustom.Normal -> it
          is GameRuleSettingUiModel.Custom -> it.copy(
            playersRule = it.playersRule.copy(
              whiteRule = it.playersRule.whiteRule.copy(isFirstCheckEnd = isFirstCheck)
            )
          )
        }
      }
    )
    viewModel.onChangeFirstCheck(turn, isFirstCheck)
    uiResult(
      state = resultUiState1,
      effects = listOf()
    )
    val isFirstCheck2 = false
    val resultUiStat2 = resultUiState1.copy(
      ruleItems = resultUiState1.ruleItems.toMutableList().map {
        when (it) {
          is GameRuleSettingUiModel.NonCustom.FirstCheck,
          is GameRuleSettingUiModel.NonCustom.Normal -> it
          is GameRuleSettingUiModel.Custom -> it.copy(
            playersRule = it.playersRule.copy(
              whiteRule = it.playersRule.whiteRule.copy(isFirstCheckEnd = isFirstCheck2)
            )
          )
        }
      }
    )
    viewModel.onChangeFirstCheck(turn, isFirstCheck2)
    uiResult(
      state = resultUiStat2,
      effects = listOf()
    )
  }

  @Test
  fun 設定ページ変更() {
    initViewModel()

    val pageIndex = 1
    val resultUiState = initUiState.copy(
      showRuleItemIndex = pageIndex,
    )
    viewModel.changePage(pageIndex)
    uiResult(
      state = resultUiState,
      effects = listOf()
    )
  }

  // TODO　ここからやる
  @Test
  fun 将棋開始_普通の将棋_１ページ目() {
    initViewModel()

    val pageIndex = 0
    val resultUiState = initUiState.copy(
      showRuleItemIndex = pageIndex,
    )
    val timeLimitCard = initUiState.ruleItems[pageIndex] as GameRuleSettingUiModel.NonCustom.Normal
    val blackHande: Hande
    val whiteHande: Hande
    when(timeLimitCard.selectedHande.turn) {
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
      ),
      playersRule =  PlayersRule(
        blackRule = PlayerRule(
          hande = blackHande,
          timeLimitRule = initUiState.timeLimitCard.blackTimeLimitRule,
          isFirstCheckEnd = false,
        ),
        whiteRule = PlayerRule(
          hande = whiteHande,
          timeLimitRule = initUiState.timeLimitCard.whiteTimeLimitRule,
          isFirstCheckEnd = false,
        ),
      ),
    )
    homeUseCase.setGameRuleLogic = {
      assertEquals(it, resultGameRule)
    }
    viewModel.changePage(pageIndex)
    viewModel.onGameStartClick()

    assertEquals(homeUseCase.calSsetGameRuleCount, 1)
    uiResult(
      state = resultUiState,
      effects = listOf()
    )
  }

  @Test
  fun 将棋開始_王手将棋_２ページ目() {
    initViewModel()

    val pageIndex = 1
    val resultUiState = initUiState.copy(
      showRuleItemIndex = pageIndex,
    )
    val timeLimitCard = initUiState.ruleItems[pageIndex] as GameRuleSettingUiModel.NonCustom.FirstCheck
    val blackHande: Hande
    val whiteHande: Hande
    when(timeLimitCard.selectedHande.turn) {
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
      ),
      playersRule =  PlayersRule(
        blackRule = PlayerRule(
          hande = blackHande,
          timeLimitRule = initUiState.timeLimitCard.blackTimeLimitRule,
          isFirstCheckEnd = true,
        ),
        whiteRule = PlayerRule(
          hande = whiteHande,
          timeLimitRule = initUiState.timeLimitCard.whiteTimeLimitRule,
          isFirstCheckEnd = true,
        ),
      ),
    )
    homeUseCase.setGameRuleLogic = {
      assertEquals(it, resultGameRule)
    }
    viewModel.changePage(pageIndex)
    viewModel.onGameStartClick()

    assertEquals(homeUseCase.calSsetGameRuleCount, 1)
    uiResult(
      state = resultUiState,
      effects = listOf()
    )
  }

  @Test
  fun 将棋開始_カスタム将棋_３ページ目() {
    initViewModel()

    val pageIndex = 2
    val resultUiState = initUiState.copy(
      showRuleItemIndex = pageIndex,
    )
    val timeLimitCard = initUiState.ruleItems[pageIndex] as GameRuleSettingUiModel.Custom
    val resultGameRule = GameRule(
      boardRule = BoardRule(
        setUpRule = BoardRule.SetUpRule.NORMAL,
      ),
      playersRule =  PlayersRule(
        blackRule = PlayerRule(
          hande = timeLimitCard.playersRule.blackRule.hande,
          timeLimitRule = initUiState.timeLimitCard.blackTimeLimitRule,
          isFirstCheckEnd = timeLimitCard.playersRule.blackRule.isFirstCheckEnd,
        ),
        whiteRule = PlayerRule(
          hande = timeLimitCard.playersRule.whiteRule.hande,
          timeLimitRule = initUiState.timeLimitCard.whiteTimeLimitRule,
          isFirstCheckEnd = timeLimitCard.playersRule.whiteRule.isFirstCheckEnd,
        ),
      ),
    )
    homeUseCase.setGameRuleLogic = {
      assertEquals(it, resultGameRule)
    }
    viewModel.changePage(pageIndex)
    viewModel.onGameStartClick()

    assertEquals(homeUseCase.calSsetGameRuleCount, 1)
    uiResult(
      state = resultUiState,
      effects = listOf()
    )
  }
}

