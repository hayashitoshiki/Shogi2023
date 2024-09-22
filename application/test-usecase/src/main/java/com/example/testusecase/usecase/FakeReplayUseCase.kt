package com.example.testusecase.usecase

import com.example.testusecase.model.fake
import com.example.usecaseinterface.model.ReplayLoadMoveRecodeParam
import com.example.usecaseinterface.model.result.ReplayInitResult
import com.example.usecaseinterface.model.result.ReplayLoadMoveRecodeResult
import com.example.usecaseinterface.usecase.ReplayUseCase

class FakeReplayUseCase : ReplayUseCase {

    private var callReplayInitCount = 0
    private var callGoNextCount = 0
    private var callGoBackCount = 0

    fun getCallReplayInitCount() : Int = callReplayInitCount
    fun getCallGoNextCount() : Int = callGoNextCount
    fun getCallGoBackCount() : Int = callGoBackCount

    var replayInitLogic: () -> ReplayInitResult = { ReplayInitResult.fake() }
    var goNextLogic: () -> ReplayLoadMoveRecodeResult = { ReplayLoadMoveRecodeResult.fake() }
    var goBackLogic: () -> ReplayLoadMoveRecodeResult = { ReplayLoadMoveRecodeResult.fake() }


    override fun replayInit(): ReplayInitResult {
        callReplayInitCount += 1
        return replayInitLogic()
    }

    override fun goNext(param: ReplayLoadMoveRecodeParam): ReplayLoadMoveRecodeResult {
        callGoNextCount += 1
        return goNextLogic()
    }

    override fun goBack(param: ReplayLoadMoveRecodeParam): ReplayLoadMoveRecodeResult {
        callGoBackCount += 1
        return goBackLogic()
    }
}
