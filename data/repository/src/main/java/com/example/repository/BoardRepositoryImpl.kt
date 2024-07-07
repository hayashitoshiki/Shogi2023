package com.example.repository

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Cell
import com.example.domainObject.game.board.Position
import javax.inject.Inject

class BoardRepositoryImpl @Inject constructor() : BoardRepository {

    private var boards: MutableMap<Map<Position, Cell>, Int> = mutableMapOf()

    override fun set(board: Board) {
        val boardCount = (boards[board.getAllCells()] ?: 0) + 1
        boards[board.getAllCells()] = boardCount
    }

    override fun get(): Map<Map<Position, Cell>, Int> {
        return boards
    }
}
