package com.example.domainLogic.board

import com.example.domainObject.game.board.Position
import com.example.domainObject.game.board.Size

operator fun Size.contains(position: Position): Boolean {
    return position.row in 1..row && position.column in 1..column
}
