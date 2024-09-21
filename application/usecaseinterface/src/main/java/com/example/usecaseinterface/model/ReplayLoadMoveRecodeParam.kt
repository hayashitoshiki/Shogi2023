package com.example.usecaseinterface.model

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.log.MoveRecode

/**
 * 棋譜を読み込む際のパラメータ
 *
 * @property board 将棋盤
 * @property blackStand 先手の持ち駒
 * @property whiteStand 後手の持ち駒
 * @property index 現在読み込んでいる棋譜のインデックス
 * @property log 棋譜
 */
data class ReplayLoadMoveRecodeParam(
  val board: Board,
  val blackStand: Stand,
  val whiteStand: Stand,
  val log: List<MoveRecode>,
  val index: Int,
)
