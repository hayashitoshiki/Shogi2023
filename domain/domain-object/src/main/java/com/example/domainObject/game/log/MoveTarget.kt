package com.example.domainObject.game.log

import com.example.domainObject.game.board.Position
import com.example.domainObject.game.piece.Piece

/**
 * 動かそうとしている駒
 *
 */
sealed interface MoveTarget {

    /**
     * 将棋盤上の駒
     *
     * @property position 座標
     */
    data class Board(
        val position: Position,
    ) : MoveTarget

    /**
     * 持ち駒台上の駒
     *
     * @property piece 持ち駒
     */
    data class Stand(
        val piece: Piece,
    ) : MoveTarget
}
