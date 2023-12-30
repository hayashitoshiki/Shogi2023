package com.example.home.game.mapper

import com.example.home.game.model.ReadyMoveInfoUiModel
import com.example.usecase.usecaseinterface.model.ReadyMoveInfoUseCaseModel

fun ReadyMoveInfoUiModel.toUseCaseModel(): ReadyMoveInfoUseCaseModel {
    return ReadyMoveInfoUseCaseModel(
        hintList = this.hintList,
        hold = this.hold.toUseCaseModel(),
    )
}