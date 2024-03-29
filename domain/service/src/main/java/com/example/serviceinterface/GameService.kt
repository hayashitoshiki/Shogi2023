package com.example.serviceinterface

import com.example.entity.game.board.Board
import com.example.entity.game.board.Position
import com.example.entity.game.board.Stand
import com.example.entity.game.piece.Piece
import com.example.entity.game.rule.GameRule
import com.example.entity.game.rule.Turn

/**
 * 将棋に関するドメインロジック
 *
 */
interface GameService {

    /**
     * 指定した手番の王様が詰んでいるか判定
     *
     * @param board 将棋盤
     * @param stand 持ち駒
     * @param turn 手番
     * @return 詰んでいるか
     */
    fun isCheckmate(board: Board, stand: Stand, turn: Turn): Boolean

    /**
     * 持ち駒の駒を打つ
     *
     * @param board 将棋盤
     * @param stand 持ち駒
     * @param turn 手番
     * @param piece 打つ駒
     * @param position 打つ場所
     */
    fun putPieceByStand(
        board: Board,
        stand: Stand,
        turn: Turn,
        piece: Piece,
        position: Position,
    ): Pair<Board, Stand>

    /**
     * 指定したマスに駒を動かす
     *
     * @param board 将棋盤
     * @param stand 持ち駒
     * @param beforePosition 動かす駒のマス目
     * @param afterPosition 動かす先のマス目
     * @return
     */
    fun movePieceByPosition(
        board: Board,
        stand: Stand,
        beforePosition: Position,
        afterPosition: Position,
    ): Pair<Board, Stand>

    /**
     * 勝利判定
     *
     * @param board 将棋盤
     * @param stand 持ち駒
     * @param turn 手番
     * @return 判定結果
     */
    fun checkGameSet(
        board: Board,
        stand: Stand,
        turn: Turn,
    ): Boolean

    /**
     * 王手将棋かつ詰み判定
     *
     * @param board 将棋盤
     * @param turn 手番
     * @param rule ルール
     * @return 王手将棋でかつ詰んでいるか
     */
    fun checkGameSetForFirstCheck(board: Board, turn: Turn, rule: GameRule): Boolean
}
