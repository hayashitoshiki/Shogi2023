package com.example.testDomainObject.log

import com.example.domainObject.game.board.Position
import com.example.domainObject.game.log.MoveRecode
import com.example.domainObject.game.log.MoveTarget
import com.example.domainObject.game.piece.Piece
import com.example.domainObject.game.rule.Turn
import com.example.testDomainObject.board.fake

fun MoveRecode.Companion.fake(
  turn: Turn = Turn.Normal.Black,
  moveTarget: MoveTarget = MoveTarget.Board.fake(),
  afterPosition: Position = Position.fake(),
  isEvolution: Boolean = false,
  takePiece: Piece? = null,
): MoveRecode = MoveRecode(
  turn = turn,
  moveTarget = moveTarget,
  afterPosition = afterPosition,
  isEvolution = isEvolution,
  takePiece = takePiece,
)
