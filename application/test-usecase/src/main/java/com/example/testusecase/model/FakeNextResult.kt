package com.example.testusecase.model

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Position
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.rule.Turn
import com.example.testDomainObject.board.fake
import com.example.testDomainObject.board.fake詰まない
import com.example.usecaseinterface.model.result.NextResult

fun NextResult.Hint.Companion.fake(
    hintPositionList: List<Position> = emptyList(),
): NextResult.Hint {
    return NextResult.Hint(
        hintPositionList = hintPositionList,
    )
}

fun NextResult.Move.Only.Companion.fake(
    board: Board = Board.fake詰まない(),
    blackStand: Stand = Stand.fake(),
    whiteStand: Stand = Stand.fake(),
    nextTurn: Turn = Turn.Normal.Black,
): NextResult.Move.Only {
    return NextResult.Move.Only(
        board = board,
        blackStand = blackStand,
        whiteStand = whiteStand,
        nextTurn = nextTurn,
    )
}

fun NextResult.Move.ChooseEvolution.Companion.fake(
    board: Board = Board.fake詰まない(),
    blackStand: Stand = Stand.fake(),
    whiteStand: Stand = Stand.fake(),
    nextTurn: Turn = Turn.Normal.Black,
): NextResult.Move.ChooseEvolution {
    return NextResult.Move.ChooseEvolution(
        board = board,
        blackStand = blackStand,
        whiteStand = whiteStand,
        nextTurn = nextTurn,
    )
}

fun NextResult.Move.Win.Companion.fake(
    board: Board = Board.fake詰まない(),
    blackStand: Stand = Stand.fake(),
    whiteStand: Stand = Stand.fake(),
    nextTurn: Turn = Turn.Normal.Black,
): NextResult.Move.Win {
    return NextResult.Move.Win(
        board = board,
        blackStand = blackStand,
        whiteStand = whiteStand,
        nextTurn = nextTurn,
    )
}

fun NextResult.Move.Drown.Companion.fake(
    board: Board = Board.fake詰まない(),
    blackStand: Stand = Stand.fake(),
    whiteStand: Stand = Stand.fake(),
    nextTurn: Turn = Turn.Normal.Black,
): NextResult.Move.Drown {
    return NextResult.Move.Drown(
        board = board,
        blackStand = blackStand,
        whiteStand = whiteStand,
        nextTurn = nextTurn,
    )
}
