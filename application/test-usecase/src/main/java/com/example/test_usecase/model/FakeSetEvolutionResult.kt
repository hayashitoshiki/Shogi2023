package com.example.test_usecase.model

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.rule.Turn
import com.example.testDomainObject.board.fake詰まない
import com.example.usecaseinterface.model.result.SetEvolutionResult

fun SetEvolutionResult.Companion.fake(
    board: Board = Board.fake詰まない(),
    isWin: Boolean = false,
    nextTurn: Turn = Turn.Normal.Black,
    isDraw: Boolean = false,
): SetEvolutionResult {
    return SetEvolutionResult(
      board = board,
      isWin = isWin,
      nextTurn = nextTurn,
      isDraw = isDraw,
    )
}