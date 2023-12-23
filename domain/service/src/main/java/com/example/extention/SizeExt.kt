package com.example.extention

import com.example.entity.game.board.Position
import com.example.entity.game.board.Size


operator fun Size.contains(position: Position): Boolean {
    return position.row in 1..row && position.column in 1..column
}
