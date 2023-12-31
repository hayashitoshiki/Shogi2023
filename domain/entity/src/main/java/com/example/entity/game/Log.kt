package com.example.entity.game

import com.example.entity.game.board.Position
import com.example.entity.game.piece.Piece
import com.example.entity.game.rule.Turn

/**
 * ログ
 *
 * @property turn 手番
 * @property moveTarget 動かそうとしている駒
 * @property afterPosition 動かした後のマス目
 * @property isEvolution 成ったか
 * @property takePiece とった駒（何もとっていない場合はnull）
 */
data class Log(
    val turn: Turn,
    val moveTarget: MoveTarget,
    val afterPosition: Position,
    val isEvolution: Boolean,
    val takePiece: Piece?,
)
