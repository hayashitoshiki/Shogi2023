package com.example.usecase.usecaseinterface.model.result

import com.example.entity.game.board.Board
import com.example.entity.game.board.Stand

/**
 * 棋譜の手番を進めた結果を返却
 *
 * @property board 将棋盤
 * @property stand 持ち駒
 */
data class ReplayGoNextResult(
    val board: Board,
    val stand: Stand,
)
