package com.example.test_repository

import com.example.entity.game.board.Board
import com.example.entity.game.board.Cell
import com.example.entity.game.board.Position
import com.example.repository.repositoryinterface.GameRepository

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
