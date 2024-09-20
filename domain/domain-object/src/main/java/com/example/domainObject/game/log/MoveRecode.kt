package com.example.domainObject.game.log

import com.example.domainObject.game.board.Position
import com.example.domainObject.game.piece.Piece
import com.example.domainObject.game.rule.Turn

/**
 * 一手ごとの棋譜
 *
 * @property turn 動かした手番
 * @property moveTarget 動かそうとしている駒
 * @property afterPosition 動かした後のマス目
 * @property isEvolution 成ったか
 * @property takePiece とった駒
 */
data class MoveRecode(
  val turn: Turn,
  val moveTarget: MoveTarget,
  val afterPosition: Position,
  val isEvolution: Boolean,
  val takePiece: Piece?,
) {
  companion object
}
