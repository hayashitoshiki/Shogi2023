package com.example.test_core

import androidx.lifecycle.ViewModel
import app.cash.turbine.test
import com.example.core.uilogic.BaseContract
import com.example.core.uilogic.BaseViewModel
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

abstract class ViewModelTest <viewModel: BaseViewModel<UiState, Effect>, UiState : BaseContract.State, Effect : BaseContract.Effect> : ViewModel() {

  lateinit var viewModel: viewModel
  protected abstract val initUiState: UiState


  @OptIn(ExperimentalCoroutinesApi::class)
  @Before
  fun setup() {
    Dispatchers.setMain(StandardTestDispatcher())
    setUpUseCase()
  }

  abstract fun setUpUseCase()

  abstract fun setUpViewModel()

  @OptIn(ExperimentalCoroutinesApi::class)
  @After
  fun tearDown() {
    Dispatchers.resetMain()
  }

  /**
   * 結果比較
   *
   * @param useCaseAsserts UseCaseの期待値
   * @param state Stateの期待値
   * @param effects Effectの期待値
   */
  protected fun result(useCaseAsserts:List<UseCaseAsserts<*>>, state: UiState, effects: List<Effect>) {
    uiResult(state, effects)
    useCaseAsserts.forEach { useCaseAssert ->
      assertEquals(useCaseAssert.expected, useCaseAssert.actual)
    }
  }

  /**
   * 実行結果比較
   *
   * @param state Stateの期待値
   * @param effects Effectの期待値
   */
  private fun uiResult(state: UiState, effects: List<Effect>)  = runTest {
    val resultState = viewModel.state.value

    // 比較
    Assert.assertEquals(resultState, state)
    // Effect
    effects.forEach { effect ->
      viewModel.effect.test {
        val item = awaitItem()
        assertEquals(effect, item)
      }
    }
  }

  /**
   * ViewModelアクション
   *
   * @param useCaseSet UseCaseの設定
   * @param action ViewModelのアクション
   */
  protected fun viewModelAction(useCaseSet: () -> Unit, action: viewModel.() -> Unit) {
    useCaseSet()
    setUpViewModel()
    action(viewModel)
  }

  /**
   * UseCaseの期待値
   *
   * @param expected 期待値
   * @param actual 実際の値
   */
  protected data class UseCaseAsserts<T>(
    val expected: T,
    val actual: T,
  )

}
