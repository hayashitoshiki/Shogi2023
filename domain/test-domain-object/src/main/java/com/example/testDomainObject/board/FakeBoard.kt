package com.example.testDomainObject.board

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.CellStatus
import com.example.domainObject.game.board.Position
import com.example.domainObject.game.piece.Piece
import com.example.domainObject.game.rule.Turn


/**
 * 下記の局面を返す
 *
 *  6 |  5   | 4
 *  　| ●玉  | 　  | 1
 *  　| 　   | 　  | 2
 *  　| ○金  | ○金 | 3
 */
fun Board.Companion.`fake●5一玉○5三金○4三金`(): Board {
    val black = Turn.Normal.Black
    val white = Turn.Normal.White
    return Board().also {
        it.update(Position(5, 1), CellStatus.Fill.FromPiece(Piece.Surface.Ou, white))
        it.update(Position(5, 3), CellStatus.Fill.FromPiece(Piece.Surface.Kin, black))
        it.update(Position(4, 3), CellStatus.Fill.FromPiece(Piece.Surface.Kin, black))
        it.update(Position(5, 9), CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, black))
    }
}

/**
 * 下記の局面を返す
 *
 *  6 |  5   | 4
 *  　| ●玉  |  | 2
 *  　| 　   |  | 3
 *  　| ○金  |  | 4
 */
fun Board.Companion.`fake●5二玉○5四金`(): Board {
    val black = Turn.Normal.Black
    val white = Turn.Normal.White
    return Board().also {
        it.update(Position(5, 2), CellStatus.Fill.FromPiece(Piece.Surface.Ou, white))
        it.update(Position(5, 4), CellStatus.Fill.FromPiece(Piece.Surface.Kin, black))
        it.update(Position(5, 9), CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, black))
    }
}

/**
 * 下記の局面を返す
 *
 *  6 |  5   | 4
 *  　| ●玉  |  | 1
 *  　| ○香  |  | 2
 *  　| ○金  |  | 3
 */
fun Board.Companion.`fake●5一玉○5二香○5三金`(): Board {
    val black = Turn.Normal.Black
    val white = Turn.Normal.White
    return Board().also {
        it.update(Position(5, 1), CellStatus.Fill.FromPiece(Piece.Surface.Ou, white))
        it.update(Position(5, 2), CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, black))
        it.update(Position(5, 3), CellStatus.Fill.FromPiece(Piece.Surface.Kin, black))
        it.update(Position(5, 9), CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, black))
    }
}

/**
 * 下記の局面を返す
 *
 *  6 |  5   | 4
 *  　|  　　 |  | 1
 *  　| ●玉  |  | 2
 *  　| ○桂  |  | 3
 *  　| ○金  |  | 4
 */
fun Board.Companion.`fake成ったら王手_詰まない`(): Board {
    return Board().also {
        val black = Turn.Normal.Black
        val white = Turn.Normal.White
        it.update(Position(5, 2), CellStatus.Fill.FromPiece(Piece.Surface.Ou, white))
        it.update(Position(5, 3), CellStatus.Fill.FromPiece(Piece.Surface.Keima, black))
        it.update(Position(5, 4), CellStatus.Fill.FromPiece(Piece.Surface.Kin, black))
        it.update(Position(5, 9), CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, black))
    }
}

/**
 * 下記の局面を返す
 *
 *  6 |  5   | 4
 *  　|  ●玉  |   | 1
 *  　|  　　  |  | 2
 *  　|  ○桂  |  | 3
 *  　|  ○金  |  | 4
 */
fun Board.Companion.`fake詰まない`(): Board {
    return Board().also {
        val black = Turn.Normal.Black
        val white = Turn.Normal.White
        it.update(Position(5, 2), CellStatus.Fill.FromPiece(Piece.Surface.Ou, white))
        it.update(Position(5, 3), CellStatus.Fill.FromPiece(Piece.Surface.Keima, black))
        it.update(Position(5, 4), CellStatus.Fill.FromPiece(Piece.Surface.Kin, black))
        it.update(Position(5, 9), CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, black))
    }
}

/**
 * 下記の局面を返す
 *
 *  3   |  2  |  1
 *  　  | ●桂 | ●玉 | 1
 *  　  | ●歩 | ●香 | 2
 *  ○角 | ○桂 | 　  | 3
 */
fun Board.Companion.`fake詰み`(): Board {
    return Board().also {
        val black = Turn.Normal.Black
        val white = Turn.Normal.White
        it.update(Position(1, 1), CellStatus.Fill.FromPiece(Piece.Surface.Ou, white))
        it.update(Position(1, 2), CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, white))
        it.update(Position(2, 1), CellStatus.Fill.FromPiece(Piece.Surface.Keima, white))
        it.update(Position(2, 2), CellStatus.Fill.FromPiece(Piece.Surface.Fu, white))
        it.update(Position(2, 3), CellStatus.Fill.FromPiece(Piece.Surface.Keima, black))
        it.update(Position(3, 3), CellStatus.Fill.FromPiece(Piece.Surface.Kaku, black))
    }
}

/**
 * 下記の局面を返す
 *
 *   3  |  2  |  1
 *  　  | 　  | ●玉 | 1
 *  　  | ●歩 | ●香 | 2
 *  ○角 | ○桂 | 　  | 3
 */
fun Board.Companion.`fake王手_詰まない`(): Board {
    return Board().also {
        val black = Turn.Normal.Black
        val white = Turn.Normal.White
        it.update(Position(1, 1), CellStatus.Fill.FromPiece(Piece.Surface.Ou, white))
        it.update(Position(1, 2), CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, white))
        it.update(Position(2, 2), CellStatus.Fill.FromPiece(Piece.Surface.Fu, white))
        it.update(Position(2, 3), CellStatus.Fill.FromPiece(Piece.Surface.Keima, black))
        it.update(Position(3, 3), CellStatus.Fill.FromPiece(Piece.Surface.Kaku, black))
    }
}


/**
 * 下記の局面を返す
 *
 *   3  |  2  |  1
 *  　  | 　  | ●玉 | 1
 *  　  | 　  | ●香 | 2
 *  　  | 　  | ○桂 | 3
 */
fun Board.Companion.`fake駒を取れる状態`(): Board {
    return Board().also {
        val black = Turn.Normal.Black
        val white = Turn.Normal.White
        it.update(Position(1, 1), CellStatus.Fill.FromPiece(Piece.Surface.Ou, white))
        it.update(Position(1, 2), CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, white))
        it.update(Position(1, 3), CellStatus.Fill.FromPiece(Piece.Surface.Keima, black))
        it.update(Position(1, 7), CellStatus.Fill.FromPiece(Piece.Surface.Keima, white))
        it.update(Position(1, 8), CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, black))
        it.update(Position(1, 9), CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, black))
    }
}

/**
 * 下記の局面を返す
 *
 *   3  |  2  |  1
 *  　  | 　  | ●玉 | 1
 *  　  | 　  | 　  | 2
 *  ○角 | 　  | 　  | 3
 */
fun Board.Companion.`fake●1一王○3三角`(): Board {
    return Board().also {
        val black = Turn.Normal.Black
        val white = Turn.Normal.White
        it.update(Position(1, 1), CellStatus.Fill.FromPiece(Piece.Surface.Ou, white))
        it.update(Position(3, 3), CellStatus.Fill.FromPiece(Piece.Surface.Kaku, black))
        it.update(Position(1, 9), CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, black))
    }
}

/**
 * 下記の局面を返す
 *
 *   6  |  5  |  4
 *  　  | 　  | 　  | 1
 *  　  | ○玉 | 　  | 2
 *  ~~~~~~~~~~~~~~~~~~
 *  ~~~~~~~~~~~~~~~~~~
 *   　 | ○王 | 　  | 8
 *   　 | 　  | 　  | 9
 */
fun Board.Companion.`fake●5二王○5八王`(): Board {
    return Board().also {
        val black = Turn.Normal.Black
        val white = Turn.Normal.White
        it.update(Position(5, 2), CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, black))
        it.update(Position(5, 8), CellStatus.Fill.FromPiece(Piece.Surface.Ou, white))
    }
}