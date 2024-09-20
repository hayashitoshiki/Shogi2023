package com.example.game.feature.replay

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.game.TimeLimit
import com.example.core.core.ViewModelTest
import com.example.domainObject.game.log.MoveRecode
import com.example.testDomainObject.log.fake
import com.example.testusecase.model.fake
import com.example.testusecase.usecase.FakeReplayUseCase
import com.example.usecaseinterface.model.result.ReplayGoBackResult
import com.example.usecaseinterface.model.result.ReplayGoNextResult
import com.example.usecaseinterface.model.result.ReplayInitResult
import kotlinx.coroutines.test.runTest
import org.junit.Test

/**
 * 将棋画面の仕様
 *
 */
class ReplayViewModelTest : ViewModelTest<ReplayViewModel, ReplayViewModel.UiState, ReplayViewModel.Effect>() {

    private lateinit var replayUseCase: FakeReplayUseCase
    override val initUiState = ReplayViewModel.UiState(
        board = Board(),
        blackStand = Stand(),
        whiteStand = Stand(),
        log = emptyList(),
        blackTimeLimit = TimeLimit.INIT,
        whiteTimeLimit = TimeLimit.INIT,
        logNextIndex = 0,
    )

    override fun setUpUseCase() {
        replayUseCase = FakeReplayUseCase()
    }

    override fun setUpViewModel() {
        viewModel = ReplayViewModel(replayUseCase)
    }

    @Test
    fun `画面へ遷移`() = runTest {
        val initResult = ReplayInitResult.fake()
        val resultUiState = initUiState.copy(
            board = initResult.board,
            blackStand = initResult.blackStand,
            whiteStand = initResult.whiteStand,
            blackTimeLimit = initResult.blackTimeLimit,
            whiteTimeLimit = initResult.whiteTimeLimit,
            log = initResult.log ?: emptyList(),
        )
        viewModelAction(
            useCaseSet = { replayUseCase.replayInitLogic = { initResult } },
            action = {},
        )
        result(
            useCaseAsserts = listOf(
                UseCaseAsserts({ replayUseCase.getCallReplayInitCount() }, 1),
            ),
            state = resultUiState,
            effects = listOf(),
        )
    }

    @Test
    fun `一手進む`() = runTest {
        val initResult = ReplayInitResult.fake(
            log = listOf(
                MoveRecode.fake(),
                MoveRecode.fake(),
            ),
        )
        val replayGoNextResult =  ReplayGoNextResult.fake()
        val resultUiState = initUiState.copy(
            board = replayGoNextResult.board,
            blackStand = replayGoNextResult.stand,
            whiteStand = initResult.whiteStand,
            blackTimeLimit = initResult.blackTimeLimit,
            whiteTimeLimit = initResult.whiteTimeLimit,
            log = initResult.log ?: emptyList(),
            logNextIndex = 1,
        )

        viewModelAction(
            useCaseSet = {
                replayUseCase.replayInitLogic = { initResult }
                replayUseCase.goNextLogic = { replayGoNextResult }
            },
            action = { tapRight() },
        )
        result(
            useCaseAsserts = listOf(
                UseCaseAsserts({ replayUseCase.getCallGoNextCount() }, 1),
            ),
            state = resultUiState,
            effects = listOf(),
        )
    }

    @Test
    fun `一手戻る`() = runTest {
        val initResult = ReplayInitResult.fake(
            log = listOf(
                MoveRecode.fake(),
                MoveRecode.fake(),
            ),
        )
        val replayGoBackResult =  ReplayGoBackResult.fake()
        val resultUiState = initUiState.copy(
            board = replayGoBackResult.board,
            blackStand = replayGoBackResult.stand,
            whiteStand = initResult.whiteStand,
            blackTimeLimit = initResult.blackTimeLimit,
            whiteTimeLimit = initResult.whiteTimeLimit,
            log = initResult.log ?: emptyList(),
            logNextIndex = 0,
        )

        viewModelAction(
            useCaseSet = {
                replayUseCase.replayInitLogic = { initResult }
                replayUseCase.goBackLogic = { replayGoBackResult }
            },
            action = {
                tapRight()
                tapLeft()
            },
        )
        result(
            useCaseAsserts = listOf(
                UseCaseAsserts({ replayUseCase.getCallGoNextCount() }, 1),
            ),
            state = resultUiState,
            effects = listOf(),
        )
    }

}
