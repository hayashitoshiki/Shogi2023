package com.example.entity.game.board

/**
 * マス目の座標
 *
 * @property row 横
 * @property column 縦
 */
data class Position(val row: Int, val column: Int) {

    fun add(position: Position): Position {
        return this.copy(row = row + position.row, column = column + position.column)
    }
}
