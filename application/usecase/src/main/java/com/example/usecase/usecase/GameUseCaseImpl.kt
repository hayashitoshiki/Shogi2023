package com.example.usecase.usecase

import com.example.entity.game.board.Stand
import com.example.entity.game.rule.PieceSetUpRule
import com.example.service.GameService
import com.example.usecase.usecaseinterface.GameUseCase
import com.example.usecase.usecaseinterface.model.GameInitResult
import javax.inject.Inject

class GameUseCaseImpl @Inject constructor() : GameUseCase {
    private val gameService = GameService()

    override fun gameInit(pieceSetUpRule: PieceSetUpRule): GameInitResult {
        val board = gameService.setUpBoard(pieceSetUpRule)
        val blackStand = Stand()
        val whiteStand = Stand()

        return GameInitResult(
            board = board,
            blackStand = blackStand,
            whiteStand = whiteStand,
        )
    }
}
