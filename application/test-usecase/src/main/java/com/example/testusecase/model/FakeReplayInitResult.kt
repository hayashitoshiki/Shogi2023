package com.example.testusecase.model

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.game.TimeLimit
import com.example.domainObject.game.log.MoveRecode
import com.example.testDomainObject.board.fake
import com.example.testDomainObject.board.fake詰まない
import com.example.testDomainObject.game.fake
import com.example.usecaseinterface.model.result.ReplayInitResult

fun ReplayInitResult.Companion.fake(
  board: Board = Board.fake詰まない(),
  blackStand: Stand = Stand.fake(),
  whiteStand: Stand = Stand.fake(),
  blackTimeLimit: TimeLimit = TimeLimit.fake(),
  whiteTimeLimit: TimeLimit = TimeLimit.fake(),
  log: List<MoveRecode>? = emptyList(),
): ReplayInitResult = ReplayInitResult(
  board = board,
  blackStand = blackStand,
  whiteStand = whiteStand,
  blackTimeLimit = blackTimeLimit,
  whiteTimeLimit = whiteTimeLimit,
  log = log,
)