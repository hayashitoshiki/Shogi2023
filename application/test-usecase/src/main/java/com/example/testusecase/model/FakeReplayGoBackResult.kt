package com.example.testusecase.model

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Stand
import com.example.testDomainObject.board.fake
import com.example.testDomainObject.board.fake詰まない
import com.example.usecaseinterface.model.result.ReplayGoBackResult

fun ReplayGoBackResult.Companion.fake(
  board: Board = Board.fake詰まない(),
  stand: Stand = Stand.fake(),
): ReplayGoBackResult = ReplayGoBackResult(
  board = board,
  stand = stand,
)
