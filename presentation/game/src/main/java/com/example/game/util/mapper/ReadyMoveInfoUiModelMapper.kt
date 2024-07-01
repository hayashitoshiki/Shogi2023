package com.example.game.util.mapper

import com.example.domainObject.game.MoveTarget
import com.example.game.util.model.ReadyMoveInfoUiModel
import com.example.usecaseinterface.model.ReadyMoveInfoUseCaseModel

fun ReadyMoveInfoUiModel.toUseCaseModel(): com.example.usecaseinterface.model.ReadyMoveInfoUseCaseModel {
    return when (val hold = this.hold) {
        is MoveTarget.Board -> com.example.usecaseinterface.model.ReadyMoveInfoUseCaseModel.Board(
            hold = hold,
            hintList = this.hintList,
        )

        is MoveTarget.Stand -> com.example.usecaseinterface.model.ReadyMoveInfoUseCaseModel.Stand(
            hold = hold,
            hintList = this.hintList,
        )
    }
}
