package com.example.usecase.usecaseinterface.model

import com.example.entity.game.board.Position
import com.example.entity.game.piece.Piece

/**
 * 選択した駒情報
 *
 */
sealed interface TouchActionUseCaseModel {

    /**
     * 将棋盤上の駒
     *
     * @property position 座標
     */
    data class Board(
        val position: Position,
    ) : TouchActionUseCaseModel

    /**
     * 持ち駒台上の駒
     *
     * @property piece 持ち駒
     */
    data class Stand(
        val piece: Piece,
    ) : TouchActionUseCaseModel
}