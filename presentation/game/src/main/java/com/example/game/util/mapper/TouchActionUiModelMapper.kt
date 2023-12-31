package com.example.game.util.mapper

import com.example.game.util.model.TouchActionUiModel
import com.example.usecase.usecaseinterface.model.TouchActionUseCaseModel

fun TouchActionUiModel.toUseCaseModel(): TouchActionUseCaseModel {
    return when (this) {
        is TouchActionUiModel.Board -> TouchActionUseCaseModel.Board(this.position)
        is TouchActionUiModel.Stand -> TouchActionUseCaseModel.Stand(this.piece)
    }
}
