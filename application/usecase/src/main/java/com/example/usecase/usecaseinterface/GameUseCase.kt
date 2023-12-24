package com.example.usecase.usecaseinterface

import com.example.entity.game.rule.PieceSetUpRule
import com.example.usecase.usecaseinterface.model.GameInitResult

/**
 * 将棋のビジネスロジック
 *
 */
interface GameUseCase {

    /**
     * ゲーム初期化
     *
     * @param pieceSetUpRule ルール
     * @return 初期設定値
     */
    fun gameInit(pieceSetUpRule: PieceSetUpRule): GameInitResult
}
