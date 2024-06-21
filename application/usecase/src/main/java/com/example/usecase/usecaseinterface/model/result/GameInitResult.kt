package com.example.usecase.usecaseinterface.model.result

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.game.TimeLimit
import com.example.domainObject.game.rule.Turn

/**
 * ゲーム初期化時の返却物
 *
 * @property board 将棋盤
 * @property blackStand 先手の持ち駒
 * @property whiteStand 後手の持ち駒
 * @property blackTimeLimit 先手の持ち時間
 * @property whiteTimeLimit 後手の持ち時間
 * @property turn 手番
 */
data class GameInitResult(
    val board: Board,
    val blackStand: Stand,
    val whiteStand: Stand,
    val blackTimeLimit: TimeLimit,
    val whiteTimeLimit: TimeLimit,
    val turn: Turn,
) {
    companion object
}
