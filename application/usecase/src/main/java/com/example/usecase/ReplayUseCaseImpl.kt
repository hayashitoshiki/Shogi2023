package com.example.usecase

import com.example.domainObject.game.Log
import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Stand
import com.example.domainLogic.board.setUp
import com.example.domainObject.game.game.TimeLimit
import com.example.repository.GameRuleRepository
import com.example.repository.LogRepository
import com.example.serviceinterface.ReplayService
import com.example.usecaseinterface.usecase.ReplayUseCase
import com.example.usecaseinterface.model.result.ReplayGoBackResult
import com.example.usecaseinterface.model.result.ReplayGoNextResult
import com.example.usecaseinterface.model.result.ReplayInitResult
import javax.inject.Inject

class ReplayUseCaseImpl @Inject constructor(
    private val logRepository: LogRepository,
    private val gameRuleRepository: GameRuleRepository,
    private val replayService: ReplayService,
) : ReplayUseCase {

    override fun replayInit(): ReplayInitResult {
        val rule = gameRuleRepository.getGameRule()
        val board = Board.setUp(rule)
        val blackStand = Stand()
        val whiteStand = Stand()
        val blackTimeLimit = TimeLimit(rule.playersRule.blackRule.timeLimitRule)
        val whiteTimeLimit = TimeLimit(rule.playersRule.blackRule.timeLimitRule)
        val log = logRepository.getLatestLog()
        return ReplayInitResult(
            board = board,
            blackStand = blackStand,
            whiteStand = whiteStand,
            blackTimeLimit = blackTimeLimit,
            whiteTimeLimit = whiteTimeLimit,
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
