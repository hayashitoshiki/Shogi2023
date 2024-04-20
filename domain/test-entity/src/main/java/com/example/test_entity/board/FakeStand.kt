package com.example.test_entity.board

import com.example.entity.game.board.Stand
import com.example.entity.game.piece.Piece

/**
 * 下記の局面を返す
 *
 *  6 |  5   | 4
 *  　| ●玉  |  | 1
 *  　| ○香  |  | 2
 *  　| ○金  |  | 3
 */
fun Stand.Companion.fake(
    isFillPiece: Boolean,
): Stand {
    return Stand().also {
        if (isFillPiece) {
            it.add(Piece.Surface.Fu)
            it.add(Piece.Surface.Kyosya)
            it.add(Piece.Surface.Keima)
            it.add(Piece.Surface.Gin)
            it.add(Piece.Surface.Kin)
            it.add(Piece.Surface.Kaku)
            it.add(Piece.Surface.Hisya)
        }
    }

}

fun Stand.Companion.fake(
    fuCount: Int = 0,
    kyoCount: Int = 0,
    keimaCount: Int = 0,
    ginCount: Int = 0,
    kinCount: Int = 0,
    kakuCount: Int = 0,
    hisyaCount: Int = 0,
): Stand {
    return Stand().also { stand ->
        repeat(fuCount) { stand.add(Piece.Surface.Fu) }
        repeat(kyoCount) { stand.add(Piece.Surface.Kyosya) }
        repeat(keimaCount) { stand.add(Piece.Surface.Keima) }
        repeat(ginCount) { stand.add(Piece.Surface.Gin) }
        repeat(kinCount) { stand.add(Piece.Surface.Kin) }
        repeat(kakuCount) { stand.add(Piece.Surface.Kaku) }
        repeat(hisyaCount) { stand.add(Piece.Surface.Hisya) }
    }
}