package com.example.usecase.usecaseinterface.model

import com.example.entity.game.board.Board
import com.example.entity.game.board.Stand

/**
 * ゲーム初期化時の返却物
 *
 * @property board 将棋盤
 * @property blackStand 先手の持ち駒
 * @property whiteStand 後手の持ち駒
 */
data class GameInitResult(
    val board: Board,
    val blackStand: Stand,
    val whiteStand: Stand,
)
