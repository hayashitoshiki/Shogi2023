package com.example.core.uilogic

/**
 * Screen(UI描画）とViewModel(UIロジック）とのコンタクト定義
 *
 */
interface BaseContract {
    /**
     * 状態管理
     *
     */
    interface State

    /**
     * UI処理発火管理
     *
     */
    interface Effect
}