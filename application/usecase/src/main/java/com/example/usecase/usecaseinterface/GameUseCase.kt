package com.example.usecase.usecaseinterface

import com.example.entity.game.MoveTarget
import com.example.entity.game.board.Board
import com.example.entity.game.board.Position
import com.example.entity.game.board.Stand
import com.example.entity.game.piece.Piece
import com.example.entity.game.rule.Turn
import com.example.usecase.usecaseinterface.model.ReadyMoveInfoUseCaseModel
import com.example.usecase.usecaseinterface.model.result.GameInitResult
import com.example.usecase.usecaseinterface.model.result.NextResult
import com.example.usecase.usecaseinterface.model.result.SetEvolutionResult

/**
 * 将棋のビジネスロジック
 *
 */
interface GameUseCase {

    /**
     * ゲーム初期化
     *
     * @return 初期設定値
     */
    fun gameInit(): GameInitResult

    /**
     * 駒を配置する
     *
     * @param board 将棋盤
     * @param stand 持ち駒
     * @param turn 手番
     * @param touchAction 盤面 or 持ち駒のタップ状態
     * @param holdMove 指し手の保持状態
     * @return 処理結果
     */
    fun movePiece(
        board: Board,
        stand: Stand,
        turn: Turn,
        touchAction: MoveTarget.Board,
        holdMove: ReadyMoveInfoUseCaseModel,
    ): NextResult

    /**
     * 盤面の駒を使用する
     *
     * @param board 将棋盤
     * @param turn 手番
     * @param position 動かす駒
     * @return 動かせるマス目
     */
    fun useBoardPiece(
        board: Board,
        turn: Turn,
        position: Position,
    ): NextResult

    /**
     * 持ち駒を使用する
     *
     * @param board 将棋盤
     * @param turn 手番
     * @param piece 使用する持ち駒
     * @return 将棋を次に進める処理
     */
    fun useStandPiece(
        board: Board,
        turn: Turn,
        piece: Piece,
    ): NextResult.Hint

    /**
     * 指定したマスの駒の成り判定の決定を反映する
     *
     * @param turn 手番
     * @param board 将棋盤
     * @param stand 相手の持ち駒
     * @param position 駒
     * @param isEvolution 成る選択をしたか
     * @return 更新後の将棋盤
     */
    fun setEvolution(
        turn: Turn,
        board: Board,
        stand: Stand,
        position: Position,
        isEvolution: Boolean,
    ): SetEvolutionResult
}
