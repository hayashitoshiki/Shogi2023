package com.example.test_repository

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Cell
import com.example.domainObject.game.board.Position
import com.example.repository.GameRepository

class FakeGameRepository : GameRepository {

    var callSetBoardLogCount = 0
        private set
    var callGetBoardLogsCount = 0
        private set

    var setBoardLogLogic: (board: Board) -> Unit = { }
    var getBoardLogsLogic: () -> Map<Map<Position, Cell>, Int> = { emptyMap() }

    override fun setBoardLog(board: Board) {
        callSetBoardLogCount += 1
        setBoardLogLogic(board)
    }

    override fun getBoardLogs(): Map<Map<Position, Cell>, Int> {
        callGetBoardLogsCount += 1
        return getBoardLogsLogic()
    }
}
