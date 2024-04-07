package com.example.repository.repository

import com.example.entity.game.board.Board
import com.example.repository.repositoryinterface.GameRepository
import javax.inject.Inject

class GameRepositoryImpl @Inject constructor() : GameRepository {

    private var boards: MutableMap<Board, Int> = mutableMapOf<Board, Int>()

    override fun setBoardLog(board: Board) {
        val boardCount = boards[board] ?: 0
        boards[board] = boardCount
    }

    override fun getBoardLogs(): Map<Board, Int> {
        return boards
    }
}
