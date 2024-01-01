package com.example.usecase.usecaseinterface

import com.example.entity.game.MoveTarget
import com.example.entity.game.board.Board
import com.example.entity.game.board.Position
import com.example.entity.game.board.Stand
import com.example.entity.game.rule.PieceSetUpRule
import com.example.entity.game.rule.Turn
import com.example.usecase.usecaseinterface.model.ReadyMoveInfoUseCaseModel
import com.example.usecase.usecaseinterface.model.result.GameInitResult
import com.example.usecase.usecaseinterface.model.result.NextResult

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

    /**
     * 将棋を進める
     *
     * @param board 将棋盤
     * @param stand 持ち駒
     * @param turn 手番
     * @param touchAction 盤面 or 持ち駒のタップ状態
     * @param holdMove 指し手の保持状態
     * @return 将棋を次に進める処理
     */
    fun next(
        board: Board,
        stand: Stand,
        turn: Turn,
        touchAction: MoveTarget,
        holdMove: ReadyMoveInfoUseCaseModel?,
    ): NextResult

    /**
     * 指定したマスの駒を成らせる
     *
     * @param board 将棋盤
     * @param position 駒
     * @return 更新後の将棋盤
     */
    fun setEvolution(board: Board, position: Position): Board
}
