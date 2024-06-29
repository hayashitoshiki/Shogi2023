package com.example.usecaseinterface.usecase

import com.example.domainObject.game.MoveTarget
import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Position
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.piece.Piece
import com.example.domainObject.game.rule.Turn
import com.example.usecaseinterface.model.ReadyMoveInfoUseCaseModel
import com.example.usecaseinterface.model.TimeLimitsUseCaseModel
import com.example.usecaseinterface.model.result.GameInitResult
import com.example.usecaseinterface.model.result.NextResult
import com.example.usecaseinterface.model.result.SetEvolutionResult
import kotlinx.coroutines.flow.StateFlow

/**
 * 将棋のビジネスロジック
 *
 */
interface GameUseCase {

    /**
     * 持ち時間の状態を監視
     *
     * @return 持ち時間の状態
     */
    fun observeUpdateTimeLimit(): StateFlow<TimeLimitsUseCaseModel?>

    /**
     * ゲーム開始
     *
     */
    fun gameStart()

    /**
     * ゲーム終了
     *
     */
    fun gameEnd()

    /**
     * ゲーム初期化
     *
     * @return 初期設定値
     */
    fun gameInit(): GameInitResult

    /**
     * 盤上の駒を動かす
     *
     * @param board 将棋盤
     * @param stand 持ち駒
     * @param turn 手番
     * @param touchAction 打とうとしている場所
     * @param holdMove 動かそうとしている盤面の駒
     * @return 処理結果
     */
    fun movePiece(
        board: Board,
        stand: Stand,
        turn: Turn,
        touchAction: MoveTarget.Board,
        holdMove: ReadyMoveInfoUseCaseModel.Board,
    ): NextResult

    /**
     * 持ち駒を打つ
     *
     * @param board 将棋盤
     * @param stand 持ち駒
     * @param turn 手番
     * @param touchAction 打とうとしている場所
     * @param holdMove 打とうとしている持ち駒
     * @return 処理結果
     */
    fun putStandPiece(
        board: Board,
        stand: Stand,
        turn: Turn,
        touchAction: MoveTarget.Board,
        holdMove: ReadyMoveInfoUseCaseModel.Stand,
    ): NextResult

    /**
     * 盤面の駒を選択する
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
     * 持ち駒を選択する
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
