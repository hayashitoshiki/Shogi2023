package com.example.serviceinterface

import com.example.entity.game.Log
import com.example.entity.game.board.Board
import com.example.entity.game.board.Stand

/**
 * 将棋の棋譜再生に関するドメインロジック
 *
 */
interface ReplayService {

    /**
     * １手進む
     *
     * @param board 将棋盤
     * @param stand 駒
     * @param log 棋譜
     * @return １手進んだ状態
     */
    fun goNext(board: Board, stand: Stand, log: Log): Pair<Board, Stand>

    /**
     * １手戻る
     *
     * @param board 将棋盤
     * @param stand 駒
     * @param log 棋譜
     * @return １手戻った状態
     */
    fun goBack(board: Board, stand: Stand, log: Log): Pair<Board, Stand>
}
