package com.example.game.util.mapper

import com.example.game.util.model.ReadyMoveInfoUiModel
import com.example.usecase.usecaseinterface.model.ReadyMoveInfoUseCaseModel

fun ReadyMoveInfoUiModel.toUseCaseModel(): ReadyMoveInfoUseCaseModel {
    return ReadyMoveInfoUseCaseModel(
        hintList = this.hintList,
        hold = this.hold.toUseCaseModel(),
    )
}