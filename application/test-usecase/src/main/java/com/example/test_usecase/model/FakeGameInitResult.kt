package com.example.test_usecase.model

import com.example.entity.game.board.Board
import com.example.entity.game.board.Stand
import com.example.entity.game.rule.Turn
import com.example.test_entity.board.fake
import com.example.test_entity.board.fake詰まない
import com.example.usecase.usecaseinterface.model.result.GameInitResult
import com.example.usecase.usecaseinterface.model.result.SetEvolutionResult

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
    turn: Turn = Turn.Normal.Black,
): GameInitResult {
    return GameInitResult(
        board = board,
        blackStand = blackStand,
        whiteStand = whiteStand,
        turn = turn,
    )
}