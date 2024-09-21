package com.example.usecase

import com.example.domainLogic.board.setUp
import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.game.TimeLimit
import com.example.repository.GameRecodeRepository
import com.example.repository.GameRuleRepository
import com.example.serviceinterface.ReplayService
import com.example.usecaseinterface.model.ReplayLoadMoveRecodeParam
import com.example.usecaseinterface.model.result.ReplayInitResult
import com.example.usecaseinterface.model.result.ReplayLoadMoveRecodeResult
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

    override fun goNext(param: ReplayLoadMoveRecodeParam): ReplayLoadMoveRecodeResult {
        val index = param.index + 1
        val log = param.log.getOrNull(index) ?: return ReplayLoadMoveRecodeResult(
            board = param.board,
            blackStand = param.blackStand,
            whiteStand = param.whiteStand,
            index = param.index
        )
        val result = replayService.goNext(param.board, param.blackStand, param.whiteStand, log)
        return ReplayLoadMoveRecodeResult(
            board = result.first,
            blackStand = result.second,
            whiteStand = result.third,
            index = index
        )
    }

    override fun goBack(param: ReplayLoadMoveRecodeParam): ReplayLoadMoveRecodeResult {
        val index = param.index
        val log = param.log.getOrNull(index) ?: return ReplayLoadMoveRecodeResult(
            board = param.board,
            blackStand = param.blackStand,
            whiteStand = param.whiteStand,
            index = param.index
        )
        val result = replayService.goBack(param.board, param.blackStand, param.whiteStand, log)
        return ReplayLoadMoveRecodeResult(
            board = result.first,
            blackStand = result.second,
            whiteStand = result.third,
            index = index - 1,
        )
    }
}
