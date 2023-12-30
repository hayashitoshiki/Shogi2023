package com.example.usecase.usecaseinterface.model.result

import com.example.entity.game.board.Board
import com.example.entity.game.board.Stand
import com.example.entity.game.rule.Turn

/**
 * ゲーム初期化時の返却物
 *
 * @property board 将棋盤
 * @property blackStand 先手の持ち駒
 * @property whiteStand 後手の持ち駒
 * @property turn 手番
 */
data class GameInitResult(
    val board: Board,
    val blackStand: Stand,
    val whiteStand: Stand,
    val turn: Turn,
)
