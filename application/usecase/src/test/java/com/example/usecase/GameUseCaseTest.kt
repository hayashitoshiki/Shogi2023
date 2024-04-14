package com.example.usecase

import com.example.entity.game.board.Board
import com.example.entity.game.board.CellStatus
import com.example.entity.game.board.Position
import com.example.entity.game.board.Stand
import com.example.entity.game.piece.Piece
import com.example.entity.game.rule.GameRule
import com.example.entity.game.rule.Turn
import com.example.extention.searchMoveBy
import com.example.extention.setUp
import com.example.service.GameServiceImpl
import com.example.test_entity.fake
import com.example.test_entity.`fake●5一玉○5二香○5三金`
import com.example.test_entity.fake成ったら王手_詰まない
import com.example.test_entity.fake王手_詰まない
import com.example.test_entity.fake詰まない
import com.example.test_entity.fake詰み
import com.example.test_repository.FakeGameRepository
import com.example.test_repository.FakeGameRuleRepository
import com.example.test_repository.FakeLogRepository
import com.example.usecase.usecase.GameUseCaseImpl
import com.example.usecase.usecaseinterface.GameUseCase
import com.example.usecase.usecaseinterface.model.result.GameInitResult
import com.example.usecase.usecaseinterface.model.result.NextResult
import com.example.usecase.usecaseinterface.model.result.SetEvolutionResult
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
    private val gameService = GameServiceImpl()

    @Before
    fun setUp() {
        gameRuleRepository = FakeGameRuleRepository()
        logRepository = FakeLogRepository()
        gameRepository = FakeGameRepository()
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
        val rule = GameRule.fake()
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

    /**
     * 成り判定結果を反映
     * ＜条件＞
     * ・成る
     * 　・詰み（成らなかったら詰まない）
     * 　・王手（王手将棋。成らなかったら王手じゃない）
     * 　・王手（本将棋。成らなかったら王手じゃない）
     * 　・千日手（成らなかったら千日手じゃない）
     * 　・何もなし
     * ・成らない
     * 　・詰み（成ったら詰まない）
     * 　・王手（王手将棋。成ったら王手じゃない）
     * 　・王手（本将棋。成ったら王手じゃない）
     * 　・千日手（成ったら千日手じゃない）
     * 　・何もなし
     */
    @Test
    fun `成り判定結果反映`() {
        // expected
        data class Param(
            val board: Board,
            val ruleIsFirstCheckEnd: Boolean,
            val isDraw: Boolean,
            val position: Position,
            val isEvolution: Boolean,
            val result: SetEvolutionResult,
        )

        val case1Board = Board.`fake●5一玉○5二香○5三金`()
        val case1Position = Position(5, 2)
        val case2_3Board = Board.`fake成ったら王手_詰まない`()
        val case2_3Position = Position(5, 3)
        val case4_5_9_10Board = Board.`fake詰まない`()
        val case4_5_9_10Position = Position(5, 3)
        val case6Board = Board.`fake詰み`()
        val case6Position = Position(5, 2)
        val case7_8Board = Board.`fake王手_詰まない`()
        val case7_8Position = Position(5, 2)

        val params = listOf(
            // 成り_詰み
            Param(
                ruleIsFirstCheckEnd = false,
                isDraw = false,
                isEvolution = true,
                board = case1Board,
                position = case1Position,
                result = SetEvolutionResult(
                    board = case1Board.copy().also {
                        val cell = CellStatus.Fill.FromPiece(
                            Piece.Reverse.Narikyo,
                            Turn.Normal.Black,
                        )
                        it.update(case1Position, cell)
                    },
                    isDraw = false,
                    isWin = true,
                    nextTurn = Turn.Normal.White,
                )
            ),
            // 成り_王手_王手将棋
            Param(
                ruleIsFirstCheckEnd = true,
                isDraw = false,
                isEvolution = true,
                board = case2_3Board,
                position = case2_3Position,
                result = SetEvolutionResult(
                    board = case2_3Board.copy().also {
                        val cell = CellStatus.Fill.FromPiece(
                            Piece.Reverse.Narikei,
                            Turn.Normal.Black,
                        )
                        it.update(case2_3Position, cell)
                    },
                    isDraw = false,
                    isWin = true,
                    nextTurn = Turn.Normal.White,
                )
            ),
            // 成り_王手_本将棋
            Param(
                ruleIsFirstCheckEnd = false,
                isDraw = false,
                isEvolution = true,
                board = case2_3Board,
                position = case2_3Position,
                result = SetEvolutionResult(
                    board = case2_3Board.copy().also {
                        val cell = CellStatus.Fill.FromPiece(
                            Piece.Reverse.Narikei,
                            Turn.Normal.Black,
                        )
                        it.update(case2_3Position, cell)
                    },
                    isDraw = false,
                    isWin = false,
                    nextTurn = Turn.Normal.White,
                )
            ),
            // 成り_千日手
            Param(
                ruleIsFirstCheckEnd = false,
                isDraw = true,
                isEvolution = true,
                board = case4_5_9_10Board,
                position = case4_5_9_10Position,
                result = SetEvolutionResult(
                    board = case4_5_9_10Board.copy().also {
                        val cell = CellStatus.Fill.FromPiece(
                            Piece.Reverse.Narikei,
                            Turn.Normal.Black,
                        )
                        it.update(case4_5_9_10Position, cell)
                    },
                    isDraw = true,
                    isWin = false,
                    nextTurn = Turn.Normal.White,
                )
            ),
            // 成り_のみ
            Param(
                ruleIsFirstCheckEnd = false,
                isDraw = false,
                isEvolution = true,
                board = case4_5_9_10Board,
                position = case4_5_9_10Position,
                result = SetEvolutionResult(
                    board = case4_5_9_10Board.copy().also {
                        val cell = CellStatus.Fill.FromPiece(
                            Piece.Reverse.Narikei,
                            Turn.Normal.Black,
                        )
                        it.update(case4_5_9_10Position, cell)
                    },
                    isDraw = false,
                    isWin = false,
                    nextTurn = Turn.Normal.White,
                )
            ),
            // 成り_詰み
            Param(
                ruleIsFirstCheckEnd = false,
                isDraw = false,
                isEvolution = true,
                board = case6Board,
                position = case6Position,
                result = SetEvolutionResult(
                    board = case6Board.copy().also {
                        val cell = CellStatus.Fill.FromPiece(
                            Piece.Reverse.Narikyo,
                            Turn.Normal.Black,
                        )
                        it.update(case6Position, cell)
                    },
                    isDraw = false,
                    isWin = true,
                    nextTurn = Turn.Normal.White,
                )
            ),
            // 成らない_王手_王手将棋
            Param(
                ruleIsFirstCheckEnd = true,
                isDraw = false,
                isEvolution = true,
                board = case7_8Board,
                position = case7_8Position,
                result = SetEvolutionResult(
                    board = case7_8Board.copy().also {
                        val cell = CellStatus.Fill.FromPiece(
                            Piece.Reverse.Narikei,
                            Turn.Normal.Black,
                        )
                        it.update(case7_8Position, cell)
                    },
                    isDraw = false,
                    isWin = true,
                    nextTurn = Turn.Normal.White,
                )
            ),
            // 成らない_王手_本将棋
            Param(
                ruleIsFirstCheckEnd = false,
                isDraw = false,
                isEvolution = true,
                board = case7_8Board,
                position = case7_8Position,
                result = SetEvolutionResult(
                    board = case7_8Board.copy().also {
                        val cell = CellStatus.Fill.FromPiece(
                            Piece.Reverse.Narikei,
                            Turn.Normal.Black,
                        )
                        it.update(case7_8Position, cell)
                    },
                    isDraw = false,
                    isWin = false,
                    nextTurn = Turn.Normal.White,
                )
            ),
            // 成らない_千日手
            Param(
                ruleIsFirstCheckEnd = false,
                isDraw = true,
                isEvolution = false,
                board = case4_5_9_10Board,
                position = case4_5_9_10Position,
                result = SetEvolutionResult(
                    board = case4_5_9_10Board.copy().also {
                        val cell = CellStatus.Fill.FromPiece(
                            Piece.Reverse.Narikei,
                            Turn.Normal.Black,
                        )
                        it.update(case4_5_9_10Position, cell)
                    },
                    isDraw = true,
                    isWin = false,
                    nextTurn = Turn.Normal.White,
                )
            ),
            // 成らない_のみ
            Param(
                ruleIsFirstCheckEnd = false,
                isDraw = false,
                isEvolution = false,
                board = case4_5_9_10Board,
                position = case4_5_9_10Position,
                result = SetEvolutionResult(
                    board = case4_5_9_10Board.copy().also {
                        val cell = CellStatus.Fill.FromPiece(
                            Piece.Reverse.Narikei,
                            Turn.Normal.Black,
                        )
                        it.update(case4_5_9_10Position, cell)
                    },
                    isDraw = false,
                    isWin = false,
                    nextTurn = Turn.Normal.White,
                )
            ),
        )

        params.forEach { param ->
            val rule = GameRule.fake(
                blackIsFirstCheckEnd = param.ruleIsFirstCheckEnd,
                whiteIsFirstCheckEnd = !param.ruleIsFirstCheckEnd,
            )

            // mock
            gameRepository.getBoardLogsLogic = {
                if (param.isDraw) {
                    mapOf(param.result.board.getAllCells() to 3)
                } else {
                    emptyMap()
                }
            }
            gameRuleRepository.getGameRuleLogic = { rule }

            // run
            val expected = gameUseCase.setEvolution(
                turn = Turn.Normal.Black,
                board = param.board,
                stand = Stand(),
                position = param.position,
                isEvolution = param.isEvolution,
            )
            // result
            Assert.assertEquals(expected, param.result)
        }
    }

    /**
     * 持ち駒使用
     *
     * ＜条件＞
     * ・なし
     *
     * ＜期待値＞
     * ・選択した持ち駒がおけるマス一覧が返却されること
     */
    @Test
    fun `持ち駒使用`() {
        data class Param(
            val piece: Piece,
        )

        val params = listOf(
            Param(piece = Piece.Surface.Fu),
            Param(piece = Piece.Surface.Kin),
        )

        params.forEach { param ->
            // run
            val turn = Turn.Normal.Black
            val board = Board().also {
                it.update(
                    Position(5, 5),
                    CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black)
                )
                it.update(
                    Position(5, 1),
                    CellStatus.Fill.FromPiece(Piece.Surface.Ou, Turn.Normal.White)
                )
                it.update(
                    Position(5, 9),
                    CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, Turn.Normal.Black)
                )
            }
            val expected = gameUseCase.useStandPiece(
                board = board,
                turn = turn,
                piece = param.piece
            )

            val hintPositionList = gameService.searchPutBy(param.piece, board, turn)
            val result = NextResult.Hint(
                hintPositionList = hintPositionList,
            )

            // result
            Assert.assertEquals(expected, result)
        }
    }

    /**
     * 盤上の駒を選択
     *
     * ＜条件＞
     * ・なし
     *
     * ＜期待値＞
     * ・選択した駒を動かせるマス一覧が返却されること
     */
    @Test
    fun `盤上の駒を選択`() {
        data class Param(
            val position: Position,
        )

        val params = listOf(
            Param(position = Position(5, 5)),
            Param(position = Position(5, 9)),
        )

        params.forEach { param ->
            val turn = Turn.Normal.Black
            val board = Board().also {
                it.update(
                    Position(5, 5),
                    CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black)
                )
                it.update(
                    Position(5, 1),
                    CellStatus.Fill.FromPiece(Piece.Surface.Ou, Turn.Normal.White)
                )
                it.update(
                    Position(5, 9),
                    CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, Turn.Normal.Black)
                )
            }
            val hintPositionList = board.searchMoveBy(param.position, turn)
            val result = NextResult.Hint(
                hintPositionList = hintPositionList,
            )

            // run
            val expected = gameUseCase.useBoardPiece(
                board = board,
                turn = turn,
                position = param.position,
            )

            // result
            Assert.assertEquals(expected, result)
        }
    }
}
