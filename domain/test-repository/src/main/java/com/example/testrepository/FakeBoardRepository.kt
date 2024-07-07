package com.example.testrepository

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Cell
import com.example.domainObject.game.board.Position
import com.example.repository.BoardRepository

class FakeBoardRepository : BoardRepository {

    var callSetCount = 0
        private set
    var callGetCount = 0
        private set

    var setLogic: (board: Board) -> Unit = { }
    var getLogic: () -> Map<Map<Position, Cell>, Int> = { emptyMap() }

    override fun set(board: Board) {
        callSetCount += 1
        setLogic(board)
    }

    override fun get(): Map<Map<Position, Cell>, Int> {
        callGetCount += 1
        return getLogic()
    }
}
