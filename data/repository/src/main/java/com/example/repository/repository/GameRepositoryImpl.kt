package com.example.repository.repository

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Cell
import com.example.domainObject.game.board.Position
import com.example.repository.repositoryinterface.GameRepository
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor() : GameRepository {

    private var boards: MutableMap<Map<Position, Cell>, Int> = mutableMapOf()

    override fun setBoardLog(board: Board) {
        val boardCount = (boards[board.getAllCells()] ?: 0) + 1
        boards[board.getAllCells()] = boardCount
    }

    override fun getBoardLogs(): Map<Map<Position, Cell>, Int> {
        return boards
    }
}
