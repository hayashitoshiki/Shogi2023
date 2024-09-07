package com.example.testusecase.usecase

import com.example.domainObject.game.log.MoveTarget
import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Position
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.piece.Piece
import com.example.domainObject.game.rule.Turn
import com.example.testusecase.model.fake
import com.example.usecaseinterface.model.ReadyMoveInfoUseCaseModel
import com.example.usecaseinterface.model.TimeLimitsUseCaseModel
import com.example.usecaseinterface.model.result.GameInitResult
import com.example.usecaseinterface.model.result.NextResult
import com.example.usecaseinterface.model.result.SetEvolutionResult
import com.example.usecaseinterface.usecase.GameUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FakeGameUseCase : GameUseCase {

    private var callObserveUpdateTimeLimitCount = 0
    private var callGameStartCount = 0
    private var callGameEndCount = 0
    private var callGameInitCount = 0
    private var callMovePieceCount = 0
    private var callPutStandPieceCount = 0
    private var callUseBoardPieceCount = 0
    private var callUseStandPieceCount = 0
    private var callSetEvolutionCount = 0

    fun getCallObserveUpdateTimeLimitCount() : Int = callObserveUpdateTimeLimitCount
    fun getCallGameStartCount() : Int = callGameStartCount
    fun getCallGameEndCount() : Int = callGameEndCount
    fun getCallGameInitCount() : Int = callGameInitCount
    fun getCallMovePieceCount() : Int = callMovePieceCount
    fun getCallPutStandPieceCount() : Int = callPutStandPieceCount
    fun getCallUseBoardPieceCount() : Int = callUseBoardPieceCount
    fun getCallUseStandPieceCount() : Int = callUseStandPieceCount
    fun getCallSetEvolutionCount() : Int = callSetEvolutionCount

    var observeUpdateTimeLimitLogic: () -> StateFlow<TimeLimitsUseCaseModel> = {
        MutableStateFlow(TimeLimitsUseCaseModel.fake())
    }
    var gameStartLogic: () -> GameInitResult = { GameInitResult.fake() }
    var gameEndLogic: () -> GameInitResult = { GameInitResult.fake() }
    var gameInitLogic: () -> GameInitResult = { GameInitResult.fake() }
    var movePieceLogic: () -> NextResult = { NextResult.Move.Only.fake() }
    var putStandPieceLogic: () -> NextResult = { NextResult.Move.Only.fake() }
    var useBoardPieceLogic: () -> NextResult = { NextResult.Move.Only.fake() }
    var useStandPieceLogic: () -> NextResult.Hint = { NextResult.Hint.fake() }
    var setEvolutionLogic: () -> SetEvolutionResult = { SetEvolutionResult.fake() }
    override fun observeUpdateTimeLimit(): StateFlow<TimeLimitsUseCaseModel?> {
        callObserveUpdateTimeLimitCount += 1
        return observeUpdateTimeLimitLogic()
    }

    override fun gameStart() {
        callGameStartCount += 1
        gameStartLogic()
    }

    override fun gameEnd() {
        callGameEndCount += 1
        gameEndLogic()
    }

    override fun gameInit(): GameInitResult {
        callGameInitCount += 1
        return gameInitLogic()
    }

    override fun movePiece(
        board: Board,
        stand: Stand,
        turn: Turn,
        touchAction: MoveTarget.Board,
        holdMove: ReadyMoveInfoUseCaseModel.Board,
    ): NextResult {
        callMovePieceCount += 1
        return movePieceLogic()
    }

    override fun putStandPiece(
        board: Board,
        stand: Stand,
        turn: Turn,
        touchAction: MoveTarget.Board,
        holdMove: ReadyMoveInfoUseCaseModel.Stand,
    ): NextResult {
        callPutStandPieceCount += 1
        return putStandPieceLogic()
    }

    override fun useBoardPiece(
        board: Board,
        turn: Turn,
        position: Position,
    ): NextResult {
        callUseBoardPieceCount += 1
        return useBoardPieceLogic()
    }

    override fun useStandPiece(
        board: Board,
        turn: Turn,
        piece: Piece,
    ): NextResult.Hint {
        callUseStandPieceCount += 1
        return useStandPieceLogic()
    }

    override fun setEvolution(
        turn: Turn,
        board: Board,
        stand: Stand,
        position: Position,
        isEvolution: Boolean,
    ): SetEvolutionResult {
        callSetEvolutionCount += 1
        return setEvolutionLogic()
    }
}
