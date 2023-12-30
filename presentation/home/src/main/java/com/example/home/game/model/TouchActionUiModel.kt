package com.example.home.game.model

import com.example.entity.game.board.Position
import com.example.entity.game.piece.Piece

/**
 * 選択したもの
 *
 */
sealed interface TouchActionUiModel {

    /**
     * 将棋盤上
     *
     * @property position 座標
     */
    data class Board(
        val position: Position,
    ) : TouchActionUiModel

    /**
     * 持ち駒台上
     *
     * @property piece 持ち駒
     */
    data class Stand(
        val piece: Piece
    ) : TouchActionUiModel
}