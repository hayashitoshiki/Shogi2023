package com.example.repository

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.Cell
import com.example.domainObject.game.board.Position

/**
 * 盤面用Repository
 *
 */
interface BoardRepository {

    /**
     * 保存
     *
     * @param board 盤面
     */
    fun set(board: Board)

    /**
     * 取得
     *
     * @return 盤面
     */
    fun get(): Map<Map<Position, Cell>, Int>
}
