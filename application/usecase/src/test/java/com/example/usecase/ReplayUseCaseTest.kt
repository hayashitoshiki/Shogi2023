package com.example.usecase

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.log.MoveRecode
import com.example.testDomainObject.board.fake
import com.example.testDomainObject.board.fake詰まない
import com.example.testDomainObject.board.fake詰み
import com.example.testDomainObject.log.fake
import com.example.test_service.FakeReplayService
import com.example.testrepository.FakeGameRecodeRepository
import com.example.testrepository.FakeGameRuleRepository
import com.example.usecaseinterface.model.ReplayLoadMoveRecodeParam
import com.example.usecaseinterface.model.result.ReplayLoadMoveRecodeResult
import com.example.usecaseinterface.usecase.ReplayUseCase
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test

class ReplayUseCaseTest {

  private lateinit var replayUseCase: ReplayUseCase
  private lateinit var gameRecodeRepository: FakeGameRecodeRepository
  private lateinit var gameRuleRepository: FakeGameRuleRepository
  private lateinit var replayService: FakeReplayService

  private val initBoard = Board.fake()
  private val initBlackStand = Stand.fake()
  private val initWhiteStand = Stand.fake()
  private val log: List<MoveRecode> = listOf(
    MoveRecode.fake(),
    MoveRecode.fake(),
    MoveRecode.fake(),
  )
  private val replayLoadMoveRecodeParam = ReplayLoadMoveRecodeParam(
    board = initBoard,
    blackStand = initBlackStand,
    whiteStand = initWhiteStand,
    log = log,
    index = 0,
  )
  private val goBackLogicResult =  Triple(
    Board.fake詰まない(),
    Stand.fake(fuCount = 1),
    Stand.fake(hisyaCount = 1),
  )
  private val goNextLogicResult =  Triple(
    Board.fake詰み(),
    Stand.fake(kinCount = 1),
    Stand.fake(ginCount = 1),
  )

  @Before
  fun setUp() {
    gameRuleRepository = FakeGameRuleRepository()
    gameRecodeRepository = FakeGameRecodeRepository()
    replayService = FakeReplayService().also {
      it.goBackLogic = { _, _, _, _ -> goBackLogicResult }
      it.goNextLogic = { _, _, _, _ -> goNextLogicResult }
    }
    replayUseCase = ReplayUseCaseImpl(
      gameRecodeRepository = gameRecodeRepository,
      gameRuleRepository = gameRuleRepository,
      replayService = replayService,
    )
  }

  @Test
  fun `１手進む`() {
    data class Param(
      val case: String,
      val param: ReplayLoadMoveRecodeParam,
      val expected: ReplayLoadMoveRecodeResult,
      val expectedCallGoNextCount: Int,
    )

    val data = listOf(
      Param(
        case = "最初に進める",
        param = replayLoadMoveRecodeParam.copy(index = -1),
        expected = ReplayLoadMoveRecodeResult(
          board = goNextLogicResult.first,
          blackStand = goNextLogicResult.second,
          whiteStand = goNextLogicResult.third,
          index = 0,
        ),
        expectedCallGoNextCount = 1,
      ),
      Param(
        case = "最後一歩手前で進める",
        param = replayLoadMoveRecodeParam.copy(index = 1),
        expected = ReplayLoadMoveRecodeResult(
          board = goNextLogicResult.first,
          blackStand = goNextLogicResult.second,
          whiteStand = goNextLogicResult.third,
          index = 2,
        ),
        expectedCallGoNextCount = 1,
      ),
      Param(
        case = "最後で進める",
        param = replayLoadMoveRecodeParam.copy(index = 2),
        expected = ReplayLoadMoveRecodeResult(
          board = initBoard,
          blackStand = initBlackStand,
          whiteStand = initWhiteStand,
          index = 2,
        ),
        expectedCallGoNextCount = 0,
      ),
    )

    data.forEach { param ->
      setUp()
      val result = replayUseCase.goNext(param.param)
      assertEquals(param.case, param.expected, result)
      assertEquals(param.case, replayService.callGoNextCount, param.expectedCallGoNextCount)
      assertEquals(param.case, replayService.callGoBackCount, 0)
    }
  }

  @Test
  fun `１手戻る`() {
    data class Param(
      val case: String,
      val param: ReplayLoadMoveRecodeParam,
      val expected: ReplayLoadMoveRecodeResult,
      val expectedCallGoBackCount: Int,
    )

    val data = listOf(
      Param(
        case = "最初で戻る",
        param = replayLoadMoveRecodeParam.copy(index = -1),
        expected = ReplayLoadMoveRecodeResult(
          board = initBoard,
          blackStand = initBlackStand,
          whiteStand = initWhiteStand,
          index = -1,
        ),
        expectedCallGoBackCount = 0,
      ),
      Param(
        case = "最初１歩手前で戻る",
        param = replayLoadMoveRecodeParam.copy(index = 0),
        expected = ReplayLoadMoveRecodeResult(
          board = goBackLogicResult.first,
          blackStand = goBackLogicResult.second,
          whiteStand = goBackLogicResult.third,
          index = -1,
        ),
        expectedCallGoBackCount = 1,
      ),
      Param(
        case = "最後で戻る",
        param = replayLoadMoveRecodeParam.copy(index = 2),
        expected = ReplayLoadMoveRecodeResult(
          board = goBackLogicResult.first,
          blackStand = goBackLogicResult.second,
          whiteStand = goBackLogicResult.third,
          index = 1,
        ),
        expectedCallGoBackCount = 1,
      ),
    )

    data.forEach { param ->
      setUp()
      val result = replayUseCase.goBack(param.param)
      assertEquals(param.case, param.expected, result)
      assertEquals(param.case, replayService.callGoNextCount, 0)
      assertEquals(param.case, replayService.callGoBackCount, param.expectedCallGoBackCount)
    }
  }
}