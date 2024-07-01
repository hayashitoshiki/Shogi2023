package com.example.test.uilogic

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

/**
 * UIロジック Base
 *
 */
abstract class BaseViewModel<UiState : BaseContract.State, Effect : BaseContract.Effect> : ViewModel() {

    private val initialState: UiState by lazy { initState() }

    // 状態管理
    private val _state: MutableState<UiState> = mutableStateOf(initialState)
    val state: State<UiState> = _state

    // エフェクト
    private val _effect: Channel<Effect> = Channel()
    val effect: Flow<Effect> = _effect.receiveAsFlow()

    /**
     * エフェクト設定
     *
     * @param builder
     */
    protected fun setEffect(builder: () -> Effect) {
        val effectValue = builder()
        viewModelScope.launch { _effect.send(effectValue) }
    }

    /**
     * 状態設定
     *
     * @param reducer
     */
    protected fun setState(reducer: UiState.() -> UiState) {
        val newState = state.value.reducer()
        _state.value = newState
    }

    /**
     * State初期化
     *
     * @return 初期化したState
     */
    abstract fun initState(): UiState
}
