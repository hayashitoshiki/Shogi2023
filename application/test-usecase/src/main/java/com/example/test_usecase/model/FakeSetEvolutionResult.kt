package com.example.test_usecase.model

import com.example.entity.game.board.Board
import com.example.entity.game.board.Position
import com.example.entity.game.rule.Turn
import com.example.test_entity.board.fake詰まない
import com.example.usecase.usecaseinterface.model.result.NextResult
import com.example.usecase.usecaseinterface.model.result.SetEvolutionResult


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