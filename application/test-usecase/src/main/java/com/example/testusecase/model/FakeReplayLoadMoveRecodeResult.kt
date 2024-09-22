package com.example.testusecase.model

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Stand
import com.example.testDomainObject.board.fake
import com.example.usecaseinterface.model.result.ReplayLoadMoveRecodeResult

fun ReplayLoadMoveRecodeResult.Companion.fake(
  board: Board = Board.fake(),
  blackStand: Stand = Stand.fake(),
  whiteStand: Stand = Stand.fake(),
  index: Int = 0,
) = ReplayLoadMoveRecodeResult(
  board = board,
  blackStand = blackStand,
  whiteStand = whiteStand,
  index = index,
)