package com.example.usecase.usecaseinterface.model.result

import com.example.entity.game.board.Board
import com.example.entity.game.board.Position
import com.example.entity.game.board.Stand
import com.example.entity.game.rule.Turn

/**
 * 将棋を進める処理の戻り値
 *
 */
sealed interface NextResult {

    /**
     * 駒を置ける場所を返却
     *
     * @property hintPositionList 駒の置ける場所リスト
     */
    data class Hint(
        val hintPositionList: List<Position>
    ) : NextResult

    /**
     * 駒を動かした後の盤面を返却
     *
     * @property board 将棋盤
     * @property stand 持ち駒
     * @property nextTurn 次の手番
     */
    data class Move(
        val board: Board,
        val stand: Stand,
        val nextTurn: Turn,
    ) : NextResult

    /**
     * 成るか選択
     *
     * @property board 将棋盤
     * @property stand 持ち駒
     * @property nextTurn 次の手番
     */
    data class ChooseEvolution(
        val board: Board,
        val stand: Stand,
        val nextTurn: Turn,
    ) : NextResult

    /**
     * 勝ち
     *
     * @property board 将棋盤
     * @property stand 持ち駒
     * @property nextTurn 次の手番
     */
    data class Win(
        val board: Board,
        val stand: Stand,
        val nextTurn: Turn,
    ) : NextResult
}
