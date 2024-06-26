package com.example.test_usecase.usecase

import com.example.domainObject.game.MoveTarget
import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Position
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.piece.Piece
import com.example.domainObject.game.rule.Turn
import com.example.test_usecase.model.fake
import kotlinx.coroutines.flow.StateFlow
import com.example.usecase.usecaseinterface.GameUseCase
import com.example.usecase.usecaseinterface.model.ReadyMoveInfoUseCaseModel
import com.example.usecase.usecaseinterface.model.TimeLimitsUseCaseModel
import com.example.usecase.usecaseinterface.model.result.GameInitResult
import com.example.usecase.usecaseinterface.model.result.NextResult
import com.example.usecase.usecaseinterface.model.result.SetEvolutionResult
import kotlinx.coroutines.flow.MutableStateFlow

class FakeGameUseCase: GameUseCase {

    var callObserveUpdateTimeLimitCount = 0
        private set
    var callGameStartCount = 0
        private set
    var callGameEndCount = 0
        private set
    var callGameInitCount = 0
        private set
    var callMovePieceCount = 0
        private set
    var callPutStandPieceCount = 0
        private set
    var callUseBoardPieceCount = 0
        private set
    var callUseStandPieceCount = 0
        private set
    var callSetEvolutionCount = 0
        private set

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
