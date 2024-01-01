package com.example.usecase.usecaseinterface.model.result

import com.example.entity.game.board.Board
import com.example.entity.game.board.Stand

/**
 * 棋譜の手番を戻した結果を返却
 *
 * @property board 将棋盤
 * @property stand 持ち駒
 */
data class ReplayGoBackResult(
    val board: Board,
    val stand: Stand,
)
