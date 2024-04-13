package com.example.usecase

import com.example.entity.game.board.Board
import com.example.entity.game.board.Stand
import com.example.entity.game.rule.BoardRule
import com.example.entity.game.rule.GameRule
import com.example.entity.game.rule.PlayerRule
import com.example.entity.game.rule.PlayersRule
import com.example.entity.game.rule.Turn
import com.example.extention.setUp
import com.example.service.GameServiceImpl
import com.example.test_repository.FakeGameRepository
import com.example.test_repository.FakeGameRuleRepository
import com.example.test_repository.FakeLogRepository
import com.example.usecase.usecase.GameUseCaseImpl
import com.example.usecase.usecaseinterface.GameUseCase
import com.example.usecase.usecaseinterface.model.result.GameInitResult
import org.junit.Assert
import org.junit.Before
import org.junit.Test

/**
 * 将棋のビジネスロジックの仕様
 *
 */
class GameUseCaseTest {

    private lateinit var gameUseCase: GameUseCase
    private lateinit var logRepository: FakeLogRepository
    private lateinit var gameRuleRepository: FakeGameRuleRepository
    private lateinit var gameRepository: FakeGameRepository

    @Before
    fun setUp() {
        gameRuleRepository = FakeGameRuleRepository()
        logRepository = FakeLogRepository()
        gameRepository = FakeGameRepository()

        val gameService = GameServiceImpl()
        gameUseCase = GameUseCaseImpl(
            logRepository = logRepository,
            gameRuleRepository = gameRuleRepository,
            gameRepository = gameRepository,
            gameService = gameService,
        )
    }

    /**
     * 盤面初期化処理
     *
     * ＜条件＞
     * ・なし
     *
     * ＜期待値＞
     * ・ルールに沿った盤面や持ち駒で初期化された値が返却されること
     * ・棋譜ログが新規作成されること
     */
    @Test
    fun `盤面初期化`() {
        // expected
        val rule = GameRule(
            boardRule = BoardRule(),
            playersRule = PlayersRule(
                blackRule = PlayerRule(),
                whiteRule = PlayerRule(),
            ),
        )
        val board = Board.setUp(rule)
        val blackStand = Stand.setUp(rule.playersRule.blackRule)
        val whiteStand = Stand.setUp(rule.playersRule.whiteRule)
        val turn = Turn.Normal.Black
        val expected = GameInitResult(
            board = board,
            blackStand = blackStand,
            whiteStand = whiteStand,
            turn = turn,
        )

        // mock
        gameRuleRepository.getGameRuleLogic = { rule }

        // run
        val result = gameUseCase.gameInit()

        // result
        Assert.assertEquals(expected, result)
        Assert.assertEquals(logRepository.callCreateGameLogCount, 1)
    }
}
