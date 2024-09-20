package com.example.testDomainObject.board

import com.example.domainObject.game.board.Position

fun Position.Companion.fake(
  row: Int = 1,
  column: Int = 1,
) = Position(
  row = row,
  column = column,
)