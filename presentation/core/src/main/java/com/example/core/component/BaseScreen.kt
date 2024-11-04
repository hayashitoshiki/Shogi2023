package com.example.core.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.runtime.State
import androidx.navigation.NavHostController
import com.example.core.uilogic.BaseContract
import com.example.core.uilogic.BaseViewModel
import kotlinx.coroutines.flow.Flow


abstract class BaseScreen<viewModel: BaseViewModel<UiState, Effect, Action>, UiState : BaseContract.State, Effect : BaseContract.Effect, Action : BaseContract.Action> {
    @Composable
    fun Screen(
        modifier: Modifier = Modifier,
        navController: NavHostController,
        viewModel: viewModel,
    ) {
        val effect = viewModel.effect
        val uiState = viewModel.state
        val action: (Action) -> Unit = { viewModel.callAction(it) }

        Effect(navController, effect, uiState, action)
        View(modifier, uiState, action)
    }

    /**
     *
     */
    @Composable
    abstract fun Effect(
        navController: NavHostController,
        effect: Flow<Effect>,
        uiState: State<UiState>,
        action: (Action) -> Unit
    )

    /**
     * 画面
     */
    @Composable
    abstract fun View(
        modifier: Modifier,
        uiState: State<UiState>,
        action: (Action) -> Unit,
    )
}