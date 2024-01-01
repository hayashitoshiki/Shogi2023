package com.example.usecase.usecaseinterface

import com.example.entity.game.Log
import com.example.entity.game.board.Board
import com.example.entity.game.board.Stand
import com.example.entity.game.rule.PieceSetUpRule
import com.example.usecase.usecaseinterface.model.result.ReplayGoBackResult
import com.example.usecase.usecaseinterface.model.result.ReplayGoNextResult
import com.example.usecase.usecaseinterface.model.result.ReplayInitResult

/**
 * 将棋のビジネスロジック
 *
 */
interface ReplayUseCase {

    /**
     * 感想戦の初期化
     *
     * @param pieceSetUpRule ルール
     * @return 初期設定値
     */
    fun replayInit(pieceSetUpRule: PieceSetUpRule): ReplayInitResult


    fun goNext(board: Board, stand: Stand, log: Log): ReplayGoNextResult

    fun goBack(board: Board, stand: Stand, log: Log): ReplayGoBackResult

}
