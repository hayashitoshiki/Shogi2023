package com.example.usecase

import com.example.domainLogic.board.setUp
import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.game.TimeLimit
import com.example.domainObject.game.log.MoveRecode
import com.example.repository.GameRecodeRepository
import com.example.repository.GameRuleRepository
import com.example.serviceinterface.ReplayService
import com.example.usecaseinterface.model.result.ReplayGoBackResult
import com.example.usecaseinterface.model.result.ReplayGoNextResult
import com.example.usecaseinterface.model.result.ReplayInitResult
import com.example.usecaseinterface.usecase.ReplayUseCase
import javax.inject.Inject

class ReplayUseCaseImpl @Inject constructor(
    private val gameRecodeRepository: GameRecodeRepository,
    private val gameRuleRepository: GameRuleRepository,
    private val replayService: ReplayService,
) : ReplayUseCase {

    override fun replayInit(): ReplayInitResult {
        val rule = gameRuleRepository.get()
        val timeLimitRule = rule.timeLimitRule
        val board = Board.setUp(rule.boardRule)
        val blackStand = Stand()
        val whiteStand = Stand()
        val blackTimeLimit = TimeLimit(timeLimitRule.blackTimeLimitRule)
        val whiteTimeLimit = TimeLimit(timeLimitRule.whiteTimeLimitRule)
        val log = gameRecodeRepository.getLast()?.moveRecodes
        return ReplayInitResult(
            board = board,
            blackStand = blackStand,
            whiteStand = whiteStand,
            blackTimeLimit = blackTimeLimit,
            whiteTimeLimit = whiteTimeLimit,
            log = log,
        )
    }

    override fun goNext(board: Board, stand: Stand, log: MoveRecode): ReplayGoNextResult {
        val result = replayService.goNext(board, stand, log)
        return ReplayGoNextResult(
            board = result.first,
            stand = result.second,
        )
    }

    override fun goBack(board: Board, stand: Stand, log: MoveRecode): ReplayGoBackResult {
        val result = replayService.goBack(board, stand, log)
        return ReplayGoBackResult(
            board = result.first,
            stand = result.second,
        )
    }
}
