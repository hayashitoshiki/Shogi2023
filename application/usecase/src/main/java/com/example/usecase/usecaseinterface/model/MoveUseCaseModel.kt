package com.example.usecase.usecaseinterface.model

import com.example.entity.game.board.Position

/**
 * 動かそうとしている駒情報
 *
 * @property hold 動かそうとしている駒
 * @property hintList 置ける場所リスト
 */
data class MoveUseCaseModel(
    val hold: TouchActionUseCaseModel,
    val hintList: List<Position>,
)
