package com.example.usecaseinterface.model.result

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Stand

/**
 * 棋譜の手番を進めた結果を返却
 *
 * @property board 将棋盤
 * @property blackStand 先手の持ち駒
 * @property whiteStand 後手の持ち駒
 * @property index 現在読み込んでいる棋譜のインデックス
 */
data class ReplayLoadMoveRecodeResult(
  val board: Board,
  val blackStand: Stand,
  val whiteStand: Stand,
  val index: Int,
) {
  companion object
}
