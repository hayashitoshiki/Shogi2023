package com.example.extention

import com.example.entity.game.board.Position

/**
 * 足す
 *
 * @param position 足すマス目
 * @return 足し算結果
 */
fun Position.add(position: Position): Position {
    return this.copy(row = row + position.row, column = column + position.column)
}

/**
 * 引く
 *
 * @param position 引くマス目
 * @return　引き算結果
 */
fun Position.minus(position: Position): Position {
    return this.copy(row = row - position.row, column = column - position.column)
}
