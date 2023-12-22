package com.example.entity.game.board

import com.example.entity.game.piece.Piece

/**
 * 持ち駒台
 *
 */
class Stand {
    private val pieceList: MutableList<Piece> = mutableListOf()

    /**
     * 持ち駒数
     */
    val pieceCount: Int get() = pieceList.size

    /**
     * 持ち駒リスト
     */
    val pieces: List<Piece> get() = pieceList

    /**
     * 持ち駒追加
     *
     * @param piece 追加する駒
     */
    fun add(piece: Piece) {
        pieceList.add(piece)
    }

    /**
     * 持ち駒削除
     *
     * @param piece 減らす駒
     */
    fun remove(piece: Piece) {
        pieceList.remove(piece)
    }

    /**
     * 持ち駒全削除
     *
     */
    fun clear() {
        pieceList.clear()
    }
}