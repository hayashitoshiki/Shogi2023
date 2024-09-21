package com.example.usecaseinterface.usecase

import com.example.usecaseinterface.model.ReplayLoadMoveRecodeParam
import com.example.usecaseinterface.model.result.ReplayInitResult
import com.example.usecaseinterface.model.result.ReplayLoadMoveRecodeResult

/**
 * 将棋のビジネスロジック
 *
 */
interface ReplayUseCase {

    /**
     * 感想戦の初期化
     *
     * @return 初期設定値
     */
    fun replayInit(): ReplayInitResult

    /**
     * 指定した棋譜を読み込み、局面を進める
     *
     * @param param 盤面を進める際のパラメータ
     * @return 進めた局面
     */
    fun goNext(param: ReplayLoadMoveRecodeParam): ReplayLoadMoveRecodeResult

    /**
     * 指定した棋譜を読み込み、局面を戻す
     *
     * @param param 盤面を戻す際のパラメータ
     * @return 戻した局面
     */
    fun goBack(param: ReplayLoadMoveRecodeParam): ReplayLoadMoveRecodeResult
}
