package com.example.game.feature.game

import com.example.domainObject.game.log.MoveTarget
import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Position
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.game.TimeLimit
import com.example.domainObject.game.piece.Piece
import com.example.domainObject.game.rule.Turn
import com.example.game.util.model.ReadyMoveInfoUiModel
import com.example.core.core.ViewModelTest
import com.example.testDomainObject.board.fake
import com.example.testDomainObject.board.`fake●5一玉○5二香○5三金`
import com.example.testDomainObject.board.fake詰まない
import com.example.testDomainObject.board.fake駒を取れる状態
import com.example.testDomainObject.game.fake
import com.example.testusecase.model.fake
import com.example.testusecase.usecase.FakeGameUseCase
import com.example.usecaseinterface.model.result.GameInitResult
import com.example.usecaseinterface.model.result.NextResult
import kotlinx.coroutines.test.runTest
import org.junit.Test

/**
 * 将棋画面の仕様
 *
 */
class GameViewModelTest : ViewModelTest<GameViewModel, GameViewModel.UiState, GameViewModel.Effect>() {

    private lateinit var gameUseCase: FakeGameUseCase
    override val initUiState = GameViewModel.UiState(
        board = Board(),
        blackStand = Stand(),
        whiteStand = Stand(),
        blackTimeLimit = TimeLimit.INIT,
        whiteTimeLimit = TimeLimit.INIT,
        turn = Turn.Normal.Black,
        readyMoveInfo = null,
    )

    override fun setUpUseCase() {
        gameUseCase = FakeGameUseCase()
    }

    override fun setUpViewModel() {
        viewModel = GameViewModel(gameUseCase)
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
        viewModelAction(
            useCaseSet = {},
            action = {},
        )
        result(
            useCaseAsserts = listOf(
                UseCaseAsserts({ gameUseCase.getCallGameInitCount() }, 1),
            ),
            state = initUiState,
            effects = listOf(),
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
            blackTimeLimit = TimeLimit.fake(),
            whiteTimeLimit = TimeLimit.fake(),
            turn = Turn.Normal.Black,
        )
        val caseUseStandPieceLogicResult = NextResult.Hint.fake(
            hintPositionList = listOf(Position(3, 3)),
        )
        val uiStateResult = initUiState.copy(
            blackStand = caseGameInitLogicResult.blackStand,
            whiteStand = caseGameInitLogicResult.whiteStand,
            readyMoveInfo = ReadyMoveInfoUiModel(
                hold = MoveTarget.Stand(
                    piece = caseTapPiece,
                ),
                hintList = caseUseStandPieceLogicResult.hintPositionList,
            ),
        )

        viewModelAction(
            useCaseSet = {
                gameUseCase.gameInitLogic = { caseGameInitLogicResult }
                gameUseCase.useStandPieceLogic = { caseUseStandPieceLogicResult }
            },
            action = {
                tapStand(caseTapPiece, Turn.Normal.Black)
            },
        )
        result(
            useCaseAsserts = listOf(
                UseCaseAsserts({ gameUseCase.getCallUseStandPieceCount() }, 1),
            ),
            state = uiStateResult,
            effects = listOf(),
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
            blackTimeLimit = TimeLimit.fake(),
            whiteTimeLimit = TimeLimit.fake(),
            turn = Turn.Normal.Black,
        )
        val caseUseBoardPieceLogicResult = NextResult.Hint.fake(
            hintPositionList = listOf(Position(3, 3)),
        )
        val uiStateResult1 = initUiState.copy(
            board = caseGameInitLogicResult.board,
            readyMoveInfo = ReadyMoveInfoUiModel(
                hold = MoveTarget.Board(
                    position = case1TapPosition,
                ),
                hintList = caseUseBoardPieceLogicResult.hintPositionList,
            ),
        )

        viewModelAction(
            useCaseSet = {
                gameUseCase.gameInitLogic = { caseGameInitLogicResult }
                gameUseCase.useBoardPieceLogic = { caseUseBoardPieceLogicResult }
            },
            action = {
                tapBoard(case1TapPosition)
            },
        )
        result(
            useCaseAsserts = listOf(
                UseCaseAsserts({ gameUseCase.getCallUseBoardPieceCount() }, 1),
            ),
            state = uiStateResult1,
            effects = listOf(),
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
        val uiStateResult2 = initUiState.copy(
            board = caseMovePieceLogicResult.board,
            blackStand = caseMovePieceLogicResult.stand,
            readyMoveInfo = null,
            turn = caseMovePieceLogicResult.nextTurn,
        )

        viewModelAction(
            useCaseSet = {
                gameUseCase.gameInitLogic = { caseGameInitLogicResult }
                gameUseCase.useBoardPieceLogic = { caseUseBoardPieceLogicResult }
                gameUseCase.movePieceLogic = { caseMovePieceLogicResult }
            },
            action = {
                tapBoard(case1TapPosition)
                tapBoard(case2TapPosition)
            },
        )
        result(
            useCaseAsserts = listOf(
                UseCaseAsserts({ gameUseCase.getCallUseBoardPieceCount() }, 1),
                UseCaseAsserts({ gameUseCase.getCallMovePieceCount() }, 1),
            ),
            state = uiStateResult2,
            effects = listOf(),
        )

        // case3
        gameUseCase = FakeGameUseCase()
        val case3TapPosition = Position(8, 8)

        viewModelAction(
            useCaseSet = {
                gameUseCase.gameInitLogic = { caseGameInitLogicResult }
                gameUseCase.useBoardPieceLogic = { caseUseBoardPieceLogicResult }
            },
            action = {
                tapBoard(case1TapPosition)
                tapBoard(case3TapPosition)
            },
        )
        result(
            useCaseAsserts = listOf(
                UseCaseAsserts({ gameUseCase.getCallUseBoardPieceCount() }, 2),
            ),
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
            effects = listOf(),
        )

        // case4
        gameUseCase = FakeGameUseCase()
        // data
        val case4TapStandPiece = Piece.Surface.Gin
        val case4UseStandPieceLogicResult = NextResult.Hint.fake(
            hintPositionList = listOf(Position(6, 7)),
        )
        val case4TapPosition = case4UseStandPieceLogicResult.hintPositionList.first()
        val case4PutStandPieceLogicResult = NextResult.Move.Only.fake(
            board = Board.`fake●5一玉○5二香○5三金`(),
            stand = Stand.fake(
                fuCount = 5,
            ),
            nextTurn = Turn.Normal.Black,
        )

        viewModelAction(
            useCaseSet = {
                gameUseCase.gameInitLogic = { caseGameInitLogicResult }
                gameUseCase.useStandPieceLogic = { case4UseStandPieceLogicResult }
                gameUseCase.putStandPieceLogic = { case4PutStandPieceLogicResult }
            },
            action = {
                tapStand(case4TapStandPiece, Turn.Normal.Black)
                tapBoard(case4TapPosition)
            },
        )
        result(
            useCaseAsserts = listOf(
                UseCaseAsserts({ gameUseCase.getCallUseStandPieceCount() }, 1),
                UseCaseAsserts({ gameUseCase.getCallPutStandPieceCount() }, 1),
            ),
            state = initUiState.copy(
                board = case4PutStandPieceLogicResult.board,
                blackStand = case4PutStandPieceLogicResult.stand,
                readyMoveInfo = null,
                turn = case4PutStandPieceLogicResult.nextTurn,
            ),
            effects = listOf(),
        )

        // case5
        gameUseCase = FakeGameUseCase()
        // data
        val case5TapStandPiece = Piece.Surface.Gin
        val case5UseStandPieceLogicResult = NextResult.Hint.fake(
            hintPositionList = listOf(Position(6, 7)),
        )
        val case5TapPosition = Position(1, 1)
        val case5UseBoardPieceLogicResult = NextResult.Hint.fake(
            hintPositionList = listOf(Position(2, 3)),
        )

        viewModelAction(
            useCaseSet = {
                gameUseCase.gameInitLogic = { caseGameInitLogicResult }
                gameUseCase.useStandPieceLogic = { case5UseStandPieceLogicResult }
                gameUseCase.useBoardPieceLogic = { case5UseBoardPieceLogicResult }
            },
            action = {
                tapStand(case5TapStandPiece, Turn.Normal.Black)
                tapBoard(case5TapPosition)
            },
        )
        result(
            useCaseAsserts = listOf(
                UseCaseAsserts({ gameUseCase.getCallUseStandPieceCount() }, 1),
                UseCaseAsserts({ gameUseCase.getCallUseBoardPieceCount() }, 1),
            ),
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
            effects = listOf(),
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

        viewModelAction(
            useCaseSet = {
                gameUseCase.gameInitLogic = { caseGameInitLogicResult }
                gameUseCase.useBoardPieceLogic = { case6UseBoardPieceLogicResult }
            },
            action = {
                tapBoard(case6TapPosition)
            },
        )
        result(
            useCaseAsserts = emptyList(),
            state = initUiState.copy(
                board = case6UseBoardPieceLogicResult.board,
                blackStand = case6UseBoardPieceLogicResult.stand,
                readyMoveInfo = null,
                turn = case6UseBoardPieceLogicResult.nextTurn,
            ),
            effects = listOf(
                GameViewModel.Effect.GameEnd.Win(Turn.Normal.Black),
            ),
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

        viewModelAction(
            useCaseSet = {
                gameUseCase.gameInitLogic = { caseGameInitLogicResult }
                gameUseCase.useBoardPieceLogic = { case7UseBoardPieceLogicResult }
            },
            action = {
                tapBoard(case7TapPosition)
            },
        )
        result(
            useCaseAsserts = emptyList(),
            state = initUiState.copy(
                board = case7UseBoardPieceLogicResult.board,
                blackStand = case7UseBoardPieceLogicResult.stand,
                readyMoveInfo = null,
                turn = case7UseBoardPieceLogicResult.nextTurn,
            ),
            effects = listOf(
                GameViewModel.Effect.GameEnd.Draw,
            ),
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

        viewModelAction(
            useCaseSet = {
                gameUseCase.gameInitLogic = { caseGameInitLogicResult }
                gameUseCase.useBoardPieceLogic = { case8UseBoardPieceLogicResult }
            },
            action = {
                tapBoard(case8TapPosition)
            },
        )
        result(
            useCaseAsserts = emptyList(),
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
            ),
        )
    }
}
