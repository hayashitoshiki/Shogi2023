package com.example.test_usecase.model

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.game.TimeLimit
import com.example.domainObject.game.rule.Turn
import com.example.testDomainObject.board.fake
import com.example.testDomainObject.game.fake
import com.example.usecase.usecaseinterface.model.result.GameInitResult

/**
 * ゲーム初期化時の返却物
 *
 * @property board 将棋盤
 * @property blackStand 先手の持ち駒
 * @property whiteStand 後手の持ち駒
 * @property turn 手番
 */
data class FakeGameInitResult(
    val board: Board,
    val blackStand: Stand,
    val whiteStand: Stand,
    val turn: Turn,
) {
    companion object
}

fun GameInitResult.Companion.fake(
    board: Board = Board(),
    blackStand: Stand = Stand.fake(),
    whiteStand: Stand = Stand.fake(),
    blackTimeLimit: TimeLimit = TimeLimit.fake(),
    whiteTimeLimit: TimeLimit = TimeLimit.fake(),
    turn: Turn = Turn.Normal.Black,
): GameInitResult {
    return GameInitResult(
        board = board,
        blackStand = blackStand,
        whiteStand = whiteStand,
        blackTimeLimit = blackTimeLimit,
        whiteTimeLimit = whiteTimeLimit,
        turn = turn,
    )
}