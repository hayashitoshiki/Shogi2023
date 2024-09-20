package com.example.testusecase.usecase

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.log.MoveRecode
import com.example.testusecase.model.fake
import com.example.usecaseinterface.model.result.ReplayGoBackResult
import com.example.usecaseinterface.model.result.ReplayGoNextResult
import com.example.usecaseinterface.model.result.ReplayInitResult
import com.example.usecaseinterface.usecase.ReplayUseCase

class FakeReplayUseCase : ReplayUseCase {

    private var callReplayInitCount = 0
    private var callGoNextCount = 0
    private var callGoBackCount = 0

    fun getCallReplayInitCount() : Int = callReplayInitCount
    fun getCallGoNextCount() : Int = callGoNextCount
    fun getCallGoBackCount() : Int = callGoBackCount

    var replayInitLogic: () -> ReplayInitResult = { ReplayInitResult.fake() }
    var goNextLogic: () -> ReplayGoNextResult = { ReplayGoNextResult.fake() }
    var goBackLogic: () -> ReplayGoBackResult = { ReplayGoBackResult.fake() }


    override fun replayInit(): ReplayInitResult {
        callReplayInitCount += 1
        return replayInitLogic()
    }

    override fun goNext(board: Board, stand: Stand, log: MoveRecode): ReplayGoNextResult {
        callGoNextCount += 1
        return goNextLogic()
    }

    override fun goBack(board: Board, stand: Stand, log: MoveRecode): ReplayGoBackResult {
        callGoBackCount += 1
        return goBackLogic()
    }
}
