package com.example.usecase.usecase

import com.example.entity.game.Log
import com.example.entity.game.board.Board
import com.example.entity.game.board.Stand
import com.example.entity.game.rule.PieceSetUpRule
import com.example.extention.setUp
import com.example.repository.repositoryinterface.GameRuleRepository
import com.example.repository.repositoryinterface.LogRepository
import com.example.service.ReplayService
import com.example.usecase.usecaseinterface.ReplayUseCase
import com.example.usecase.usecaseinterface.model.result.ReplayGoBackResult
import com.example.usecase.usecaseinterface.model.result.ReplayGoNextResult
import com.example.usecase.usecaseinterface.model.result.ReplayInitResult
import javax.inject.Inject

class ReplayUseCaseImpl @Inject constructor(
    private val logRepository: LogRepository,
    private val gameRuleRepository: GameRuleRepository,
) : ReplayUseCase {

    private val replayService = ReplayService()

    override fun replayInit(pieceSetUpRule: PieceSetUpRule): ReplayInitResult {
        val rule = gameRuleRepository.getGameRule()
        val board = Board.setUp(rule.pieceSetUpRule)
        val blackStand = Stand()
        val whiteStand = Stand()
        val log = logRepository.getLatestLog()
        return ReplayInitResult(
            board = board,
            blackStand = blackStand,
            whiteStand = whiteStand,
            log = log,
        )
    }

    override fun goNext(board: Board, stand: Stand, log: Log): ReplayGoNextResult {
        val result = replayService.goNext(board, stand, log)
        return ReplayGoNextResult(
            board = result.first,
            stand = result.second,
        )
    }

    override fun goBack(board: Board, stand: Stand, log: Log): ReplayGoBackResult {
        val result = replayService.goBack(board, stand, log)
        return ReplayGoBackResult(
            board = result.first,
            stand = result.second,
        )
    }
}
