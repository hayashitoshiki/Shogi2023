package com.example.repository

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Cell
import com.example.domainObject.game.board.Position

/**
 * ゲーム中しか用いない一時保持用のRepository
 *
 */
interface BoardRepository {

    /**
     * 盤面保存
     *
     * @param board 保存する盤面
     */
    fun set(board: Board)

    /**
     * 保存してある場面リストの取得
     *
     * @return 保持している盤面リスト
     */
    fun get(): Map<Map<Position, Cell>, Int>
}
