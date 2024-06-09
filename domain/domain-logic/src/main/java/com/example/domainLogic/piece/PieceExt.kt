package com.example.domainLogic.piece

import com.example.entity.game.board.Board
import com.example.entity.game.board.Position
import com.example.entity.game.piece.Piece
import com.example.entity.game.rule.Turn

/**
 * 駒を打つことができるか
 *
 * @param board 将棋盤
 * @param position マス目
 * @param turn 手番
 * @return 駒を打てるか
 */
fun Piece.isAvailablePut(board: Board, position: Position, turn: Turn): Boolean {
    return when (this) {
        Piece.Reverse.Narigin,
        Piece.Reverse.Narikei,
        Piece.Reverse.Narikyo,
        Piece.Reverse.Ryu,
        Piece.Reverse.To,
        Piece.Reverse.Uma,
        Piece.Surface.Gin,
        Piece.Surface.Gyoku,
        Piece.Surface.Hisya,
        Piece.Surface.Kaku,
        Piece.Surface.Kin,
        Piece.Surface.Ou -> true

        is Piece.Surface.Keima -> !this.shouldEvolution(board, position, turn)

        is Piece.Surface.Fu -> {
            val allCells = board.getAllCells().keys.toList()
            val filterRows = allCells.filter {
                board.getPieceOrNullByPosition(it)?.let { cell ->
                    cell.piece == Piece.Surface.Fu && cell.turn == turn
                } ?: false
            }.map { it.row }
            !this.shouldEvolution(board, position, turn) && !filterRows.contains(position.row)
        }

        is Piece.Surface.Kyosya -> !this.shouldEvolution(board, position, turn)
    }
}

/**
 * 駒が強制的にならないといけないか
 *
 * @param board 将棋盤
 * @param position おくマス目
 * @param turn 手番
 * @return 強制的にならないといけないか
 */
fun Piece.Surface.shouldEvolution(
    board: Board,
    position: Position,
    turn: Turn,
): Boolean {
    val boardMaxColumn = board.size.column
    val column = position.column

    return when (this) {
        Piece.Surface.Fu,
        Piece.Surface.Kyosya -> {
            when (turn) {
                Turn.Normal.Black -> column <= 1
                Turn.Normal.White -> boardMaxColumn <= column
            }
        }

        Piece.Surface.Keima -> {
            return when (turn) {
                Turn.Normal.Black -> column <= 2
                Turn.Normal.White -> boardMaxColumn - 1 <= column
            }
        }

        Piece.Surface.Gin,
        Piece.Surface.Gyoku,
        Piece.Surface.Hisya,
        Piece.Surface.Kaku,
        Piece.Surface.Kin,
        Piece.Surface.Ou -> false
    }
}

/**
 * 成り
 *
 * @return 裏面
 */
fun Piece.Surface.evolution(): Piece.Reverse? {
    return when (this) {
        Piece.Surface.Fu -> Piece.Reverse.To
        Piece.Surface.Gin -> Piece.Reverse.Narigin
        Piece.Surface.Hisya -> Piece.Reverse.Ryu
        Piece.Surface.Kaku -> Piece.Reverse.Uma
        Piece.Surface.Keima -> Piece.Reverse.Narikei
        Piece.Surface.Kyosya -> Piece.Reverse.Narikyo
        Piece.Surface.Kin,
        Piece.Surface.Gyoku,
        Piece.Surface.Ou -> null
    }
}

/**
 * 退化
 *
 * @return 表面
 */
fun Piece.Reverse.degeneracy(): Piece.Surface {
    return when (this) {
        Piece.Reverse.Narigin -> Piece.Surface.Gyoku
        Piece.Reverse.Narikei -> Piece.Surface.Keima
        Piece.Reverse.Narikyo -> Piece.Surface.Kyosya
        Piece.Reverse.Ryu -> Piece.Surface.Hisya
        Piece.Reverse.To -> Piece.Surface.Fu
        Piece.Reverse.Uma -> Piece.Surface.Kaku
    }
}
