package com.example.test_service

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.log.MoveRecode
import com.example.serviceinterface.ReplayService

class FakeReplayService: ReplayService {

  var callGoNextCount = 0
    private set
  var callGoBackCount = 0
    private set

  var goNextLogic: (board: Board, blackStand: Stand, whiteStand: Stand, log: MoveRecode) -> Triple<Board, Stand, Stand> = { board, blackStand, whiteStand, _ ->
    Triple(board, blackStand, whiteStand)
  }
  var goBackLogic: (board: Board, blackStand: Stand, whiteStand: Stand, log: MoveRecode) -> Triple<Board, Stand, Stand> = { board, blackStand, whiteStand, _ ->
    Triple(board, blackStand, whiteStand)
  }

  override fun goNext(
    board: Board,
    blackStand: Stand,
    whiteStand: Stand,
    log: MoveRecode
  ): Triple<Board, Stand, Stand> {
    callGoNextCount += 1
    return goNextLogic(board, blackStand, whiteStand, log)
  }

  override fun goBack(
    board: Board,
    blackStand: Stand,
    whiteStand: Stand,
    log: MoveRecode
  ): Triple<Board, Stand, Stand> {
    callGoBackCount += 1
    return goBackLogic(board, blackStand, whiteStand, log)
  }
}