package com.example.game.util.mapper

import com.example.domainObject.game.MoveTarget
import com.example.game.util.model.ReadyMoveInfoUiModel
import com.example.usecase.usecaseinterface.model.ReadyMoveInfoUseCaseModel

fun ReadyMoveInfoUiModel.toUseCaseModel(): ReadyMoveInfoUseCaseModel {
    return when (val hold = this.hold) {
        is MoveTarget.Board -> ReadyMoveInfoUseCaseModel.Board(
            hold = hold,
            hintList = this.hintList,
        )

        is MoveTarget.Stand -> ReadyMoveInfoUseCaseModel.Stand(
            hold = hold,
            hintList = this.hintList,
        )
    }
}