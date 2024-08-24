package com.example.usecase

import com.example.domainLogic.board.searchMoveBy
import com.example.domainLogic.board.setUp
import com.example.domainObject.game.MoveTarget
import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.CellStatus
import com.example.domainObject.game.board.Position
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.game.TimeLimit
import com.example.domainObject.game.piece.Piece
import com.example.domainObject.game.rule.GameRule
import com.example.domainObject.game.rule.Turn
import com.example.service.GameServiceImpl
import com.example.testDomainObject.board.fake
import com.example.testDomainObject.board.`fake●1一王○3三角`
import com.example.testDomainObject.board.`fake●5一玉○5三金○4三金`
import com.example.testDomainObject.board.`fake●5一玉○5二香○5三金`
import com.example.testDomainObject.board.`fake●5二玉○5四金`
import com.example.testDomainObject.board.`fake●5二王○5八王`
import com.example.testDomainObject.board.fake成ったら王手_詰まない
import com.example.testDomainObject.board.fake詰まない
import com.example.testDomainObject.board.fake駒を取れる状態
import com.example.testDomainObject.rule.fake
import com.example.testDomainObject.rule.fakeFromLogicRuleFirstCheckEnd
import com.example.testrepository.FakeBoardRepository
import com.example.testrepository.FakeGameRuleRepository
import com.example.testrepository.FakeLogRepository
import com.example.usecaseinterface.model.result.GameInitResult
import com.example.usecaseinterface.model.result.NextResult
import com.example.usecaseinterface.model.result.SetEvolutionResult
import com.example.usecaseinterface.usecase.GameUseCase
import kotlinx.coroutines.test.TestScope
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
    private lateinit var gameRepository: FakeBoardRepository
    private val gameService = GameServiceImpl()

    @Before
    fun setUp() {
        val coroutineScope = TestScope()
        gameRuleRepository = FakeGameRuleRepository()
        logRepository = FakeLogRepository()
        gameRepository = FakeBoardRepository()
        gameUseCase = GameUseCaseImpl(
            logRepository = logRepository,
            gameRuleRepository = gameRuleRepository,
            boardRepository = gameRepository,
            gameService = gameService,
            coroutineScope = coroutineScope,
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
    fun 盤面初期化() {
        // expected
        val rule = GameRule.fake()
        val board = Board.setUp(rule.boardRule)
        val blackStand = Stand.setUp()
        val whiteStand = Stand.setUp()
        val blackTimeLimit = TimeLimit(rule.timeLimitRule.blackTimeLimitRule)
        val whiteTimeLimit = TimeLimit(rule.timeLimitRule.whiteTimeLimitRule)
        val turn = Turn.Normal.Black
        val expected = GameInitResult(
            board = board,
            blackStand = blackStand,
            whiteStand = whiteStand,
            blackTimeLimit = blackTimeLimit,
            whiteTimeLimit = whiteTimeLimit,
            turn = turn,
        )

        // mock
        gameRuleRepository.getLogic = { rule }

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
    fun 成り判定結果反映() {
        // expected
        data class Param(
            val testCaseTitle: String,
            val board: Board,
            val ruleIsFirstCheckEnd: Boolean,
            val isDraw: Boolean,
            val position: Position,
            val isEvolution: Boolean,
            val result: SetEvolutionResult,
        )

        val case1Board = Board.`fake●5一玉○5二香○5三金`()
        val case1Position = Position(5, 2)
        val case2_3Board = Board.fake成ったら王手_詰まない()
        val case2_3Position = Position(5, 3)
        val case4_5_9_10Board = Board.fake詰まない()
        val case4_5_9_10Position = Position(5, 3)
        val case6Board = Board.`fake●5一玉○5二香○5三金`()
        val case6Position = Position(5, 2)
        val case7_8Board = Board.`fake●5一玉○5二香○5三金`()
        val case7_8Position = Position(5, 2)

        val params = listOf(
            Param(
                testCaseTitle = "成り_詰み",
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
                ),
            ),
            Param(
                testCaseTitle = "成り_王手_王手将棋",
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
                ),
            ),
            Param(
                testCaseTitle = "成り_王手_本将棋",
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
                ),
            ),
            Param(
                testCaseTitle = "成り_千日手",
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
                ),
            ),
            Param(
                testCaseTitle = "成り_のみ",
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
                ),
            ),
            Param(
                testCaseTitle = "成らない_詰み",
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
                ),
            ),
            Param(
                testCaseTitle = "成らない_王手_王手将棋",
                ruleIsFirstCheckEnd = true,
                isDraw = false,
                isEvolution = false,
                board = case7_8Board,
                position = case7_8Position,
                result = SetEvolutionResult(
                    board = case7_8Board.copy().also {
                        val cell = CellStatus.Fill.FromPiece(
                            Piece.Surface.Kyosya,
                            Turn.Normal.Black,
                        )
                        it.update(case7_8Position, cell)
                    },
                    isDraw = false,
                    isWin = true,
                    nextTurn = Turn.Normal.White,
                ),
            ),
            Param(
                testCaseTitle = "成らない_王手_本将棋",
                ruleIsFirstCheckEnd = false,
                isDraw = false,
                isEvolution = false,
                board = case7_8Board,
                position = case7_8Position,
                result = SetEvolutionResult(
                    board = case7_8Board.copy().also {
                        val cell = CellStatus.Fill.FromPiece(
                            Piece.Surface.Kyosya,
                            Turn.Normal.Black,
                        )
                        it.update(case7_8Position, cell)
                    },
                    isDraw = false,
                    isWin = false,
                    nextTurn = Turn.Normal.White,
                ),
            ),
            Param(
                testCaseTitle = "成らない_千日手",
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
                ),
            ),
            Param(
                testCaseTitle = "成らない_のみ",
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
                ),
            ),
        )

        params.forEach { param ->
            val rule = GameRule.fakeFromLogicRuleFirstCheckEnd(
                blackRule = param.ruleIsFirstCheckEnd,
                whiteRule = param.ruleIsFirstCheckEnd,
            )

            // mock
            gameRepository.getLogic = {
                if (param.isDraw) {
                    mapOf(param.result.board.getAllCells() to 3)
                } else {
                    emptyMap()
                }
            }
            gameRuleRepository.getLogic = { rule }

            // run
            val expected = gameUseCase.setEvolution(
                turn = Turn.Normal.Black,
                board = param.board,
                stand = Stand(),
                position = param.position,
                isEvolution = param.isEvolution,
            )
            // result
            Assert.assertEquals(param.testCaseTitle, expected, param.result)
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
    fun 持ち駒使用() {
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
                    CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black),
                )
                it.update(
                    Position(5, 1),
                    CellStatus.Fill.FromPiece(Piece.Surface.Ou, Turn.Normal.White),
                )
                it.update(
                    Position(5, 9),
                    CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, Turn.Normal.Black),
                )
            }
            val expected = gameUseCase.useStandPiece(
                board = board,
                turn = turn,
                piece = param.piece,
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
    fun 盤上の駒を選択() {
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
                    CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black),
                )
                it.update(
                    Position(5, 1),
                    CellStatus.Fill.FromPiece(Piece.Surface.Ou, Turn.Normal.White),
                )
                it.update(
                    Position(5, 9),
                    CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, Turn.Normal.Black),
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

    /**
     * 持ち駒を打つ
     *
     * ＜case＞
     * ・case1：何もなし
     * ・case2：千日手
     * ・case3：王手_王手将棋じゃない
     * ・case4：王手_王手将棋
     * ・case5：詰み
     *
     * ＜期待値＞
     * ・case1：持ち駒から打った盤面と持ち駒の残が反映されること
     * ・case2：引き分けが返却されること
     * ・case3：そのまま続行されること
     * ・case4：勝ち判定が返却されること
     * ・case5：勝ち判定が返却されること
     */
    @Test
    fun 持ち駒を打つ() {
        data class Param(
            val testCaseTitle: String,
            val position: Position,
            val board: Board,
            val rule: GameRule,
            val isDraw: Boolean,
            val result: NextResult.Move,
        )

        val myTurn = Turn.Normal.Black
        val nextTurn = Turn.Normal.White
        val stand = Stand.fake(isFillPiece = true)
        val resultStand = stand.copy().also {
            it.remove(Piece.Surface.Kin)
        }

        val case1Rule = GameRule.fakeFromLogicRuleFirstCheckEnd(
            blackRule = true,
        )
        val case1Board = Board.`fake●5二玉○5四金`()
        val case1Position = Position(5, 7)
        val case1ResultBoard = case1Board.copy().also {
            it.update(case1Position, CellStatus.Fill.FromPiece(Piece.Surface.Kin, myTurn))
        }
        val case2Rule = GameRule.fake()
        val case2Board = Board.fake詰まない()
        val case2Position = Position(5, 5)
        val case2ResultBoard = case2Board.copy().also {
            it.update(case2Position, CellStatus.Fill.FromPiece(Piece.Surface.Kin, myTurn))
        }
        val case3Rule = GameRule.fakeFromLogicRuleFirstCheckEnd(
            blackRule = true,
        )
        val case3Board = Board.`fake●5二玉○5四金`()
        val case3Position = Position(5, 3)
        val case3ResultBoard = case3Board.copy().also {
            it.update(case3Position, CellStatus.Fill.FromPiece(Piece.Surface.Kin, myTurn))
        }
        val case4Rule = GameRule.fake()
        val case4Board = Board.`fake●5二玉○5四金`()
        val case4Position = Position(5, 3)
        val case4ResultBoard = case4Board.copy().also {
            it.update(case4Position, CellStatus.Fill.FromPiece(Piece.Surface.Kin, myTurn))
        }
        val case5Rule = GameRule.fakeFromLogicRuleFirstCheckEnd(
            blackRule = true,
        )
        val case5Board = Board.`fake●5一玉○5三金○4三金`()
        val case5Position = Position(5, 2)
        val case5ResultBoard = case5Board.copy().also {
            it.update(case5Position, CellStatus.Fill.FromPiece(Piece.Surface.Kin, myTurn))
        }
        val params = listOf(
            Param(
                testCaseTitle = "何もなし",
                position = case1Position,
                board = case1Board,
                rule = case1Rule,
                isDraw = false,
                result = NextResult.Move.Only(
                    board = case1ResultBoard,
                    stand = resultStand,
                    nextTurn = nextTurn,
                ),
            ),
            Param(
                testCaseTitle = "千日手",
                position = case2Position,
                board = case2Board,
                rule = case2Rule,
                isDraw = true,
                result = NextResult.Move.Drown(
                    board = case2ResultBoard,
                    stand = resultStand,
                    nextTurn = nextTurn,
                ),
            ),
            Param(
                testCaseTitle = "王手_王手将棋じゃない",
                position = case3Position,
                board = case3Board,
                rule = case3Rule,
                isDraw = false,
                result = NextResult.Move.Win(
                    board = case3ResultBoard,
                    stand = resultStand,
                    nextTurn = nextTurn,
                ),
            ),
            Param(
                testCaseTitle = "王手_王手将棋",
                position = case4Position,
                board = case4Board,
                rule = case4Rule,
                isDraw = false,
                result = NextResult.Move.Only(
                    board = case4ResultBoard,
                    stand = resultStand,
                    nextTurn = nextTurn,
                ),
            ),
            Param(
                testCaseTitle = "詰み",
                position = case5Position,
                board = case5Board,
                rule = case5Rule,
                isDraw = false,
                result = NextResult.Move.Win(
                    board = case5ResultBoard,
                    stand = resultStand,
                    nextTurn = nextTurn,
                ),
            ),
        )

        params.forEach { param ->
            // data
            val hintPositionList = listOf(param.position)
            val hold = com.example.usecaseinterface.model.ReadyMoveInfoUseCaseModel.Stand(
                hold = MoveTarget.Stand(Piece.Surface.Kin),
                hintList = hintPositionList,
            )
            // mock
            gameRepository.getLogic = {
                if (param.isDraw) {
                    mapOf(param.result.board.getAllCells() to 3)
                } else {
                    emptyMap()
                }
            }
            gameRuleRepository.getLogic = { param.rule }

            // run
            val expected = gameUseCase.putStandPiece(
                board = param.board,
                turn = myTurn,
                holdMove = hold,
                stand = stand,
                touchAction = MoveTarget.Board(param.position),
            )

            // result
            Assert.assertEquals(param.testCaseTitle, expected, param.result)
        }
    }

    /**
     * 盤上の駒を動かす
     *
     * ＜case＞
     * ・case1：何もなし
     * ・case2：千日手
     * ・case3：王手_王手将棋じゃない
     * ・case4：王手_王手将棋
     * ・case5：詰み
     * ・case6：駒を取る
     * ・case7：強制的に成る
     * ・case8：成れる
     * ・case9：トライルール
     *
     * ＜期待値＞
     * ・case1：持ち駒から打った盤面と持ち駒の残が反映されること
     * ・case2：引き分けが返却されること
     * ・case3：そのまま続行されること
     * ・case4：勝ち判定が返却されること
     * ・case5：勝ち判定が返却されること
     * ・case6：駒をとった状態が反映されること
     * ・case7：動かした駒がなった状態で返却されること
     * ・case8：成り判定が返却されること
     * ・case9：勝ち判定が返却されること
     */
    @Test
    fun 盤上の駒を動かす() {
        data class Param(
            val testCaseTitle: String,
            val movePosition: Position,
            val holdPosition: Position,
            val board: Board,
            val rule: GameRule,
            val isDraw: Boolean,
            val result: NextResult.Move,
        )

        val myTurn = Turn.Normal.Black
        val nextTurn = Turn.Normal.White
        val stand = Stand.fake(isFillPiece = true)

        val case1ResultStand = stand.copy()
        val case1Rule = GameRule.fake()
        val case1Board = Board.`fake●5二玉○5四金`()
        val case1HoldPosition = Position(5, 4)
        val case1MovePosition = Position(4, 4)
        val case1ResultBoard = case1Board.copy().also {
            it.update(case1HoldPosition, CellStatus.Empty)
            it.update(case1MovePosition, CellStatus.Fill.FromPiece(Piece.Surface.Kin, myTurn))
        }
        val case2ResultStand = stand.copy()
        val case2Rule = GameRule.fake()
        val case2Board = Board.`fake●5二玉○5四金`()
        val case2HoldPosition = Position(5, 4)
        val case2MovePosition = Position(4, 4)
        val case2ResultBoard = case2Board.copy().also {
            it.update(case2HoldPosition, CellStatus.Empty)
            it.update(case2MovePosition, CellStatus.Fill.FromPiece(Piece.Surface.Kin, myTurn))
        }
        val case3ResultStand = stand.copy()
        val case3Rule = GameRule.fakeFromLogicRuleFirstCheckEnd(
            blackRule = true,
        )
        val case3Board = Board.`fake●5二玉○5四金`()
        val case3HoldPosition = Position(5, 4)
        val case3MovePosition = Position(5, 3)
        val case3ResultBoard = case3Board.copy().also {
            it.update(case3HoldPosition, CellStatus.Empty)
            it.update(case3MovePosition, CellStatus.Fill.FromPiece(Piece.Surface.Kin, myTurn))
        }
        val case4ResultStand = stand.copy()
        val case4Rule = GameRule.fakeFromLogicRuleFirstCheckEnd(
            blackRule = false,
        )
        val case4Board = Board.`fake●5二玉○5四金`()
        val case4HoldPosition = Position(5, 4)
        val case4MovePosition = Position(5, 3)
        val case4ResultBoard = case4Board.copy().also {
            it.update(case4HoldPosition, CellStatus.Empty)
            it.update(case4MovePosition, CellStatus.Fill.FromPiece(Piece.Surface.Kin, myTurn))
        }
        val case5ResultStand = stand.copy()
        val case5Rule = GameRule.fake()
        val case5Board = Board.`fake●5一玉○5三金○4三金`()
        val case5HoldPosition = Position(4, 3)
        val case5MovePosition = Position(5, 2)
        val case5ResultBoard = case5Board.copy().also {
            it.update(case5HoldPosition, CellStatus.Empty)
            it.update(case5MovePosition, CellStatus.Fill.FromPiece(Piece.Surface.Kin, myTurn))
        }
        val case6ResultStand = stand.copy().also {
            it.add(Piece.Surface.Keima)
        }
        val case6Rule = GameRule.fake()
        val case6Board = Board.fake駒を取れる状態()
        val case6HoldPosition = Position(1, 8)
        val case6MovePosition = Position(1, 7)
        val case6ResultBoard = case6Board.copy().also {
            it.update(case6HoldPosition, CellStatus.Empty)
            it.update(case6MovePosition, CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, myTurn))
        }
        val case7ResultStand = stand.copy()
        val case7Rule = GameRule.fake()
        val case7Board = Board.fake駒を取れる状態()
        val case7HoldPosition = Position(1, 3)
        val case7MovePosition = Position(2, 1)
        val case7ResultBoard = case7Board.copy().also {
            it.update(case7HoldPosition, CellStatus.Empty)
            it.update(case7MovePosition, CellStatus.Fill.FromPiece(Piece.Reverse.Narikei, myTurn))
        }
        val case8NextTurn = Turn.Normal.Black
        val case8ResultStand = stand.copy()
        val case8Rule = GameRule.fake()
        val case8Board = Board.`fake●1一王○3三角`()
        val case8HoldPosition = Position(3, 3)
        val case8MovePosition = Position(4, 2)
        val case8ResultBoard = case8Board.copy().also {
            it.update(case8HoldPosition, CellStatus.Empty)
            it.update(case8MovePosition, CellStatus.Fill.FromPiece(Piece.Surface.Kaku, myTurn))
        }
        val case9ResultStand = stand.copy()
        val case9Rule = GameRule.fake()
        val case9Board = Board.`fake●5二王○5八王`()
        val case9HoldPosition = Position(5, 2)
        val case9MovePosition = Position(5, 1)
        val case9ResultBoard = case9Board.copy().also {
            it.update(case9HoldPosition, CellStatus.Empty)
            it.update(case9MovePosition, CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, myTurn))
        }

        val params = listOf(
            Param(
                testCaseTitle = "何もなし",
                movePosition = case1MovePosition,
                holdPosition = case1HoldPosition,
                board = case1Board,
                rule = case1Rule,
                isDraw = false,
                result = NextResult.Move.Only(
                    board = case1ResultBoard,
                    stand = case1ResultStand,
                    nextTurn = nextTurn,
                ),
            ),
            Param(
                testCaseTitle = "千日手",
                movePosition = case2MovePosition,
                holdPosition = case2HoldPosition,
                board = case2Board,
                rule = case2Rule,
                isDraw = true,
                result = NextResult.Move.Drown(
                    board = case2ResultBoard,
                    stand = case2ResultStand,
                    nextTurn = nextTurn,
                ),
            ),
            Param(
                testCaseTitle = "王手_王手将棋じゃない",
                movePosition = case3MovePosition,
                holdPosition = case3HoldPosition,
                board = case3Board,
                rule = case3Rule,
                isDraw = false,
                result = NextResult.Move.Win(
                    board = case3ResultBoard,
                    stand = case3ResultStand,
                    nextTurn = nextTurn,
                ),
            ),
            Param(
                testCaseTitle = "王手_王手将棋",
                movePosition = case4MovePosition,
                holdPosition = case4HoldPosition,
                board = case4Board,
                rule = case4Rule,
                isDraw = false,
                result = NextResult.Move.Only(
                    board = case4ResultBoard,
                    stand = case4ResultStand,
                    nextTurn = nextTurn,
                ),
            ),
            Param(
                testCaseTitle = "詰み",
                movePosition = case5MovePosition,
                holdPosition = case5HoldPosition,
                board = case5Board,
                rule = case5Rule,
                isDraw = false,
                result = NextResult.Move.Win(
                    board = case5ResultBoard,
                    stand = case5ResultStand,
                    nextTurn = nextTurn,
                ),
            ),
            Param(
                testCaseTitle = "駒を取る",
                movePosition = case6MovePosition,
                holdPosition = case6HoldPosition,
                board = case6Board,
                rule = case6Rule,
                isDraw = false,
                result = NextResult.Move.Only(
                    board = case6ResultBoard,
                    stand = case6ResultStand,
                    nextTurn = nextTurn,
                ),
            ),
            Param(
                testCaseTitle = "強制的に成る",
                movePosition = case7MovePosition,
                holdPosition = case7HoldPosition,
                board = case7Board,
                rule = case7Rule,
                isDraw = false,
                result = NextResult.Move.Only(
                    board = case7ResultBoard,
                    stand = case7ResultStand,
                    nextTurn = nextTurn,
                ),
            ),
            Param(
                testCaseTitle = "成れる",
                movePosition = case8MovePosition,
                holdPosition = case8HoldPosition,
                board = case8Board,
                rule = case8Rule,
                isDraw = false,
                result = NextResult.Move.ChooseEvolution(
                    board = case8ResultBoard,
                    stand = case8ResultStand,
                    nextTurn = case8NextTurn,
                ),
            ),
            Param(
                testCaseTitle = "トライルール",
                movePosition = case9MovePosition,
                holdPosition = case9HoldPosition,
                board = case9Board,
                rule = case9Rule,
                isDraw = false,
                result = NextResult.Move.Win(
                    board = case9ResultBoard,
                    stand = case9ResultStand,
                    nextTurn = nextTurn,
                ),
            ),
        )

        params.forEach { param ->
            // data
            val hintPositionList = listOf(param.movePosition)
            val hold = com.example.usecaseinterface.model.ReadyMoveInfoUseCaseModel.Board(
                hold = MoveTarget.Board(param.holdPosition),
                hintList = hintPositionList,
            )
            // mock
            gameRepository.getLogic = {
                if (param.isDraw) {
                    mapOf(param.result.board.getAllCells() to 3)
                } else {
                    emptyMap()
                }
            }
            gameRuleRepository.getLogic = { param.rule }

            // run
            val expected = gameUseCase.movePiece(
                board = param.board,
                turn = myTurn,
                holdMove = hold,
                stand = stand,
                touchAction = MoveTarget.Board(param.movePosition),
            )

            // result
            Assert.assertEquals(expected, param.result)
        }
    }
}
