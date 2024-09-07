package com.example.usecaseinterface.model.result

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.game.TimeLimit
import com.example.domainObject.game.log.MoveRecode

/**
 * ゲーム初期化時の返却物
 *
 * @property board 将棋盤
 * @property blackStand 先手の持ち駒
 * @property whiteStand 後手の持ち駒
 * @property log 対局ログ
 */
data class ReplayInitResult(
    val board: Board,
    val blackStand: Stand,
    val whiteStand: Stand,
    val blackTimeLimit: TimeLimit,
    val whiteTimeLimit: TimeLimit,
    val log: List<MoveRecode>?,
)
