package com.example.usecase.usecaseinterface

import com.example.entity.game.rule.PieceSetUpRule

/**
 * 将棋のビジネスロジック
 *
 */
interface HomeUseCase {

    /**
     * ルール登録
     *
     * @param pieceSetUpRule ルール
     */
    fun setGameRule(pieceSetUpRule: PieceSetUpRule.Normal)
}
