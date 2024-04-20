package com.example.usecase.usecaseinterface.model.result

import com.example.entity.game.board.Board
import com.example.entity.game.rule.Turn

/**
 * 成り判定した結果を返却
 *
 * @property board 成り判定の結果を反映させた将棋盤
 * @property isWin 勝利したか
 * @property isDraw 千日手か
 */
data class SetEvolutionResult(
    val board: Board,
    val isWin: Boolean,
    val nextTurn: Turn,
    val isDraw: Boolean,
) {
    companion object
}
