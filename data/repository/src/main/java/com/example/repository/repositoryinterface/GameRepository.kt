package com.example.repository.repositoryinterface

import com.example.entity.game.board.Board

/**
 * ゲーム中しか用いない一時保持用のRepository
 *
 */
interface GameRepository {

    /**
     * 盤面保存
     *
     * @param board 保存する盤面
     */
    fun setBoardLog(board: Board)

    /**
     * 保存してある場面リストの取得
     *
     * @return 保持している盤面リスト
     */
    fun getBoardLogs(): Map<Board, Int>
}
