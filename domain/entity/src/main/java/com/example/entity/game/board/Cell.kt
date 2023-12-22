package com.example.entity.game.board

import com.example.entity.game.piece.Piece
import com.example.entity.game.rule.Turn

/**
 * マス目
 *
 * @property status 詳細
 */
data class Cell(private var status: CellStatus = CellStatus.Empty) {

    /**
     * マス目の状態取得
     *
     * @return マス目の状態
     */
    fun getStatus(): CellStatus = status

    /**
     * マス目の状態更新
     *
     * @param status 更新する状態
     */
    fun update(status: CellStatus) {
        this.status = status
    }
}

/**
 * マスの状態
 *
 */
sealed interface CellStatus {

    /**
     * 空
     */
    data object Empty : CellStatus

    /**
     * 埋まっている
     *
     */
    sealed interface Fill : CellStatus {

        /**
         * コマで埋まっている
         *
         * @property piece 駒
         * @property turn 手番
         */
        data class FromPiece(
            val piece: Piece,
            val turn: Turn,
        ) : Fill
    }
}