package com.example.serviceinterface

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.log.MoveRecode

/**
 * 将棋の棋譜再生に関するドメインロジック
 *
 */
interface ReplayService {

    /**
     * １手進む
     *
     * @param board 将棋盤
     * @param blackStand 先手の持ち駒
     * @param whiteStand 後手の持ち駒
     * @param log 棋譜
     * @return １手進んだ状態
     */
    fun goNext(board: Board, blackStand: Stand, whiteStand: Stand, log: MoveRecode): Triple<Board, Stand, Stand>

    /**
     * １手戻る
     *
     * @param board 将棋盤
     * @param blackStand 先手の持ち駒
     * @param whiteStand 後手の持ち駒
     * @param log 棋譜
     * @return １手戻った状態
     */
    fun goBack(board: Board, blackStand: Stand, whiteStand: Stand, log: MoveRecode): Triple<Board, Stand, Stand>
}

