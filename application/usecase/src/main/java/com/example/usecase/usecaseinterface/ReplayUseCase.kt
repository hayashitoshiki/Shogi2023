package com.example.usecase.usecaseinterface

import com.example.entity.game.Log
import com.example.entity.game.board.Board
import com.example.entity.game.board.Stand
import com.example.usecase.usecaseinterface.model.result.ReplayGoBackResult
import com.example.usecase.usecaseinterface.model.result.ReplayGoNextResult
import com.example.usecase.usecaseinterface.model.result.ReplayInitResult

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
     * @param board 将棋盤
     * @param stand 持ち駒
     * @param log 読み込む棋譜
     * @return 進めた局面
     */
    fun goNext(board: Board, stand: Stand, log: Log): ReplayGoNextResult

    /**
     * 指定した棋譜を読み込み、局面を戻す
     *
     * @param board 将棋盤
     * @param stand 持ち駒
     * @param log 読み込む棋譜
     * @return 戻した局面
     */
    fun goBack(board: Board, stand: Stand, log: Log): ReplayGoBackResult
}
