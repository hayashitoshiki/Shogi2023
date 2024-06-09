package com.example.game.feature.game

import app.cash.turbine.test
import com.example.domainObject.game.MoveTarget
import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Position
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.piece.Piece
import com.example.domainObject.game.rule.Turn
import com.example.game.util.model.ReadyMoveInfoUiModel
import com.example.testDomainObject.board.fake
import com.example.testDomainObject.board.`fake●5一玉○5二香○5三金`
import com.example.testDomainObject.board.fake詰まない
import com.example.testDomainObject.board.fake駒を取れる状態
import com.example.test_usecase.model.fake
import com.example.test_usecase.usecase.FakeGameUseCase
import com.example.usecase.usecaseinterface.model.result.GameInitResult
import com.example.usecase.usecaseinterface.model.result.NextResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * 将棋画面の仕様
 *
 */
class GameViewModelTest {

    private lateinit var gameViewModel: GameViewModel
    private var gameUseCase = FakeGameUseCase()

    private val initUiState = GameViewModel.UiState(
        board = Board(),
        blackStand = Stand(),
        whiteStand = Stand(),
        turn = Turn.Normal.Black,
        readyMoveInfo = null,
    )

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun setUp() {
        gameViewModel = GameViewModel(gameUseCase)
    }

    /**
     * 実行結果比較
     *
     * @param state Stateの期待値
     * @param effects Effectの期待値
     */
    private fun uiResult(state: GameViewModel.UiState, effects: List<GameViewModel.Effect>)  = runTest {
        val resultState = gameViewModel.state.value

        // 比較
        assertEquals(resultState, state)
        // Effect
        effects.forEach { effect ->
            gameViewModel.effect.test {
                val item = awaitItem()
                assertEquals(effect, item)
            }
        }

    }

    /**
     * 画面へ遷移した時の動作
     *
     * ＜条件＞
     * case1：画面へ遷移
     *
     * ＜期待値＞
     * case1：
     *  - 盤面初期化が呼ばれる
     *  - 画面の状態が初期状態になる
     */
    @Test
    fun `画面へ遷移`() = runTest {
        setUp()

        // result
        assertEquals(gameUseCase.callGameInitCount, 1)
        uiResult(
            state = initUiState,
            effects = listOf()
        )
    }

    /**
     * 持ち駒をタップ
     *
     * ＜条件＞
     * case1：持ち駒をタップ
     *
     * ＜期待値＞
     * case1：持ち駒を使用する動作の回数が1回呼ばれること
     */
    @Test
    fun `持ち駒をタップ`() = runTest {
        // data
        val caseTapPiece = Piece.Surface.Gin
        val caseGameInitLogicResult = GameInitResult(
            board = Board(),
            blackStand = Stand.fake(isFillPiece = true),
            whiteStand = Stand.fake(isFillPiece = true),
            turn = Turn.Normal.Black,
        )
        val caseUseStandPieceLogicResult = NextResult.Hint.fake(
            hintPositionList = listOf(Position(3, 3))
        )

        // run
        gameUseCase.gameInitLogic = { caseGameInitLogicResult }
        gameUseCase.useStandPieceLogic = { caseUseStandPieceLogicResult }
        setUp()
        gameViewModel.tapStand(caseTapPiece, Turn.Normal.Black)

        // result
        assertEquals(gameUseCase.callUseStandPieceCount, 1)
        uiResult(
            state = initUiState.copy(
                blackStand = caseGameInitLogicResult.blackStand,
                whiteStand = caseGameInitLogicResult.whiteStand,
                readyMoveInfo = ReadyMoveInfoUiModel(
                    hold = MoveTarget.Stand(
                        piece = caseTapPiece,
                    ),
                    hintList = caseUseStandPieceLogicResult.hintPositionList,
                )
            ),
            effects = listOf()
        )
    }

    /**
     * 盤面の駒をタップ
     *
     * ＜条件＞
     * case1：盤面の駒をタップ
     * case2：盤面の駒を選択している状態で置くことの出来る盤面のマスをタップ
     * case3：盤面の駒を選択している状態で置くことの出来ない盤面のマスをタップ
     * case4：持ち駒を選択している状態で置くことの出来る盤面のマスをタップ
     * case5：持ち駒を選択している状態で置くことの出来ない盤面のマスをタップ
     * case6：盤面の駒をタップ（勝ち）
     * case7：盤面の駒をタップ（引き分け）
     * case8：盤面の駒をタップ（成れる）
     *
     * ＜期待値＞
     * case1：盤上の駒を使用する動作の回数が1回呼ばれ、その結果が反映されること
     * case2：盤上の駒を使用する動作の回数が1回呼ばれ、盤上の駒を動かす動作の回数が1回呼ばれ、その結果が反映されること
     * case3：盤上の駒を使用する動作の回数が2回呼ばれ、盤上の駒を動かす動作の回数が1回呼ばれ、その結果が反映されること
     * case4：持ち駒をおく動作の回数が1回呼ばれ、盤上の駒を動かす動作の回数が1回呼ばれ、その結果が反映されること
     * case5：持ち駒をおく動作の回数が1回呼ばれ、盤上の駒を使用する動作の回数が1回呼ばれ、その結果が反映されること
     * case6：勝ちのEffectが発火されること
     * case7：引き分けのEffectが発火されること
     * case8：成りのEffectが発火されること
     */
    @Test
    fun `盤面の駒をタップ`() = runTest {
        val case1TapPosition = Position(5, 3)
        val caseGameInitLogicResult = GameInitResult(
            board = Board.fake詰まない(),
            blackStand = Stand.fake(),
            whiteStand = Stand.fake(),
            turn = Turn.Normal.Black,
        )
        val caseUseBoardPieceLogicResult = NextResult.Hint.fake(
            hintPositionList = listOf(Position(3, 3))
        )

        gameUseCase.gameInitLogic = { caseGameInitLogicResult }
        gameUseCase.useBoardPieceLogic = { caseUseBoardPieceLogicResult }
        setUp()
        gameViewModel.tapBoard(case1TapPosition)

        // result
        assertEquals(gameUseCase.callUseBoardPieceCount, 1)
        uiResult(
            state = initUiState.copy(
                board = caseGameInitLogicResult.board,
                readyMoveInfo = ReadyMoveInfoUiModel(
                    hold = MoveTarget.Board(
                        position = case1TapPosition,
                    ),
                    hintList = caseUseBoardPieceLogicResult.hintPositionList,
                ),
            ),
            effects = listOf()
        )

        // case2
        gameUseCase = FakeGameUseCase()
        val case2TapPosition = caseUseBoardPieceLogicResult.hintPositionList.first()
        val caseMovePieceLogicResult = NextResult.Move.Only.fake(
            board = Board.fake駒を取れる状態(),
            stand = Stand.fake(
                fuCount = 3,
            ),
            nextTurn = Turn.Normal.White,
        )

        gameUseCase.gameInitLogic = { caseGameInitLogicResult }
        gameUseCase.useBoardPieceLogic = { caseUseBoardPieceLogicResult }
        gameUseCase.movePieceLogic = { caseMovePieceLogicResult }
        setUp()
        gameViewModel.tapBoard(case1TapPosition)
        gameViewModel.tapBoard(case2TapPosition)

        // result
        assertEquals(gameUseCase.callUseBoardPieceCount, 1)
        assertEquals(gameUseCase.callMovePieceCount, 1)
        uiResult(
            state = initUiState.copy(
                board = caseMovePieceLogicResult.board,
                blackStand = caseMovePieceLogicResult.stand,
                readyMoveInfo = null,
                turn = caseMovePieceLogicResult.nextTurn,
            ),
            effects = listOf()
        )

        // case3
        gameUseCase = FakeGameUseCase()
        val case3TapPosition = Position(8, 8)

        gameUseCase.gameInitLogic = { caseGameInitLogicResult }
        gameUseCase.useBoardPieceLogic = { caseUseBoardPieceLogicResult }
        setUp()
        gameViewModel.tapBoard(case1TapPosition)
        gameViewModel.tapBoard(case3TapPosition)

        // result
        assertEquals(gameUseCase.callUseBoardPieceCount, 2)
        uiResult(
            state = initUiState.copy(
                board = caseGameInitLogicResult.board,
                blackStand = caseGameInitLogicResult.blackStand,
                readyMoveInfo = ReadyMoveInfoUiModel(
                    hold = MoveTarget.Board(
                        position = case3TapPosition,
                    ),
                    hintList = caseUseBoardPieceLogicResult.hintPositionList,
                ),
                turn = caseGameInitLogicResult.turn,
            ),
            effects = listOf()
        )

        // case4
        gameUseCase = FakeGameUseCase()
        // data
        val case4TapStandPiece = Piece.Surface.Gin
        val case4UseStandPieceLogicResult = NextResult.Hint.fake(
            hintPositionList = listOf(Position(6, 7))
        )
        val case4TapPosition = case4UseStandPieceLogicResult.hintPositionList.first()
        val case4PutStandPieceLogicResult = NextResult.Move.Only.fake(
            board = Board.`fake●5一玉○5二香○5三金`(),
            stand = Stand.fake(
                fuCount = 5,
            ),
            nextTurn = Turn.Normal.Black,
        )

        // run
        gameUseCase.gameInitLogic = { caseGameInitLogicResult }
        gameUseCase.useStandPieceLogic = { case4UseStandPieceLogicResult }
        gameUseCase.putStandPieceLogic = { case4PutStandPieceLogicResult }
        setUp()
        gameViewModel.tapStand(case4TapStandPiece, Turn.Normal.Black)
        gameViewModel.tapBoard(case4TapPosition)

        // result
        assertEquals(gameUseCase.callUseStandPieceCount, 1)
        assertEquals(gameUseCase.callPutStandPieceCount, 1)
        uiResult(
            state = initUiState.copy(
                board = case4PutStandPieceLogicResult.board,
                blackStand = case4PutStandPieceLogicResult.stand,
                readyMoveInfo = null,
                turn = case4PutStandPieceLogicResult.nextTurn,
            ),
            effects = listOf()
        )

        // case5
        gameUseCase = FakeGameUseCase()
        // data
        val case5TapStandPiece = Piece.Surface.Gin
        val case5UseStandPieceLogicResult = NextResult.Hint.fake(
            hintPositionList = listOf(Position(6, 7))
        )
        val case5TapPosition = Position(1, 1)
        val case5UseBoardPieceLogicResult = NextResult.Hint.fake(
            hintPositionList = listOf(Position(2, 3))
        )

        // run
        gameUseCase.gameInitLogic = { caseGameInitLogicResult }
        gameUseCase.useStandPieceLogic = { case5UseStandPieceLogicResult }
        gameUseCase.useBoardPieceLogic = { case5UseBoardPieceLogicResult }
        setUp()
        gameViewModel.tapStand(case5TapStandPiece, Turn.Normal.Black)
        gameViewModel.tapBoard(case5TapPosition)

        // result
        assertEquals(gameUseCase.callUseStandPieceCount, 1)
        assertEquals(gameUseCase.callUseBoardPieceCount, 1)
        uiResult(
            state = initUiState.copy(
                board = caseGameInitLogicResult.board,
                blackStand = caseGameInitLogicResult.blackStand,
                readyMoveInfo = ReadyMoveInfoUiModel(
                    hold = MoveTarget.Board(
                        position = case5TapPosition,
                    ),
                    hintList = case5UseBoardPieceLogicResult.hintPositionList,
                ),
                turn = caseGameInitLogicResult.turn,
            ),
            effects = listOf()
        )


        // case6
        val case6TapPosition = Position(5, 3)
        val case6UseBoardPieceLogicResult = NextResult.Move.Win.fake(
            board = Board.`fake●5一玉○5二香○5三金`(),
            stand = Stand.fake(
                fuCount = 5,
            ),
            nextTurn = Turn.Normal.White,
        )

        gameUseCase.gameInitLogic = { caseGameInitLogicResult }
        gameUseCase.useBoardPieceLogic = { case6UseBoardPieceLogicResult }
        setUp()
        gameViewModel.tapBoard(case6TapPosition)

        // result
        uiResult(
            state = initUiState.copy(
                board = case6UseBoardPieceLogicResult.board,
                blackStand = case6UseBoardPieceLogicResult.stand,
                readyMoveInfo = null,
                turn = case6UseBoardPieceLogicResult.nextTurn,
            ),
            effects = listOf(
                GameViewModel.Effect.GameEnd.Win(Turn.Normal.Black),
            )
        )

        // case7
        val case7TapPosition = Position(5, 3)
        val case7UseBoardPieceLogicResult = NextResult.Move.Drown.fake(
            board = Board.`fake●5一玉○5二香○5三金`(),
            stand = Stand.fake(
                fuCount = 5,
            ),
            nextTurn = Turn.Normal.White,
        )

        gameUseCase.gameInitLogic = { caseGameInitLogicResult }
        gameUseCase.useBoardPieceLogic = { case7UseBoardPieceLogicResult }
        setUp()
        gameViewModel.tapBoard(case7TapPosition)

        // result
        uiResult(
            state = initUiState.copy(
                board = case7UseBoardPieceLogicResult.board,
                blackStand = case7UseBoardPieceLogicResult.stand,
                readyMoveInfo = null,
                turn = case7UseBoardPieceLogicResult.nextTurn,
            ),
            effects = listOf(
                GameViewModel.Effect.GameEnd.Draw,
            )
        )

        // case7
        val case8TapPosition = Position(5, 3)
        val case8UseBoardPieceLogicResult = NextResult.Move.ChooseEvolution.fake(
            board = Board.`fake●5一玉○5二香○5三金`(),
            stand = Stand.fake(
                fuCount = 5,
            ),
            nextTurn = Turn.Normal.White,
        )

        gameUseCase.gameInitLogic = { caseGameInitLogicResult }
        gameUseCase.useBoardPieceLogic = { case8UseBoardPieceLogicResult }
        setUp()
        gameViewModel.tapBoard(case8TapPosition)

        // result
        uiResult(
            state = initUiState.copy(
                board = case8UseBoardPieceLogicResult.board,
                blackStand = case8UseBoardPieceLogicResult.stand,
                readyMoveInfo = null,
                turn = case8UseBoardPieceLogicResult.nextTurn,
            ),
            effects = listOf(
                GameViewModel.Effect.Evolution(
                    position = case8TapPosition,
                ),
            )
        )
    }
}
