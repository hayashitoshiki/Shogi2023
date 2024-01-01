package com.example.usecase.usecaseinterface.model.result

import com.example.entity.game.Log
import com.example.entity.game.board.Board
import com.example.entity.game.board.Stand

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
    val log: List<Log>?,
)
