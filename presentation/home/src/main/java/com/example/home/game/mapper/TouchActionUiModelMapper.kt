package com.example.home.game.mapper

import com.example.home.game.model.TouchActionUiModel
import com.example.usecase.usecaseinterface.model.TouchActionUseCaseModel

fun TouchActionUiModel.toUseCaseModel(): TouchActionUseCaseModel {
    return when (this) {
        is TouchActionUiModel.Board -> TouchActionUseCaseModel.Board(this.position)
        is TouchActionUiModel.Stand -> TouchActionUseCaseModel.Stand(this.piece)
    }
}
