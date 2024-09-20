package com.example.testDomainObject.log

import com.example.domainObject.game.board.Position
import com.example.domainObject.game.log.MoveTarget
import com.example.domainObject.game.piece.Piece
import com.example.testDomainObject.board.fake

fun MoveTarget.Board.Companion.fake(
  position: Position = Position.fake(),
) = MoveTarget.Board(
  position = position,
)

fun MoveTarget.Stand.Companion.fake(
  piece: Piece = Piece.Surface.Fu,
) = MoveTarget.Stand(
  piece = piece,
)