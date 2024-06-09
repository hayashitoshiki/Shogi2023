package com.example.usecase.usecaseinterface.model

import com.example.domainObject.game.MoveTarget
import com.example.domainObject.game.board.Position

/**
 * 動かそうとしている駒情報
 *
 * @property hold 動かそうとしている駒
 * @property hintList 置ける場所リスト
 */
sealed interface ReadyMoveInfoUseCaseModel {
    val hold: MoveTarget
    val hintList: List<Position>

    /**
     * 盤上の駒を動かそうとしている
     */
    data class Board(
        override val hold: MoveTarget.Board,
        override val hintList: List<Position>,
    ) : ReadyMoveInfoUseCaseModel

    /**
     * 持ち駒を打とうとしている
     */
    data class Stand(
        override val hold: MoveTarget.Stand,
        override val hintList: List<Position>,
    ) : ReadyMoveInfoUseCaseModel
}
