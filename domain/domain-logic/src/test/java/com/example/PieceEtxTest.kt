package com.example

import com.example.domainLogic.piece.isAvailablePut
import com.example.domainLogic.piece.shouldEvolution
import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.CellStatus
import com.example.domainObject.game.board.Position
import com.example.domainObject.game.piece.Piece
import com.example.domainObject.game.rule.Turn
import org.junit.Assert
import org.junit.Test

class PieceEtxTest {

    @Test
    fun `駒が強制的にならないといけないか`() {
        data class Param(
            val casePiece: Piece.Surface,
            val casePosition: Position,
            val caseTurn: Turn,
            val result: Boolean,
        )

        val params = listOf(
            // 先手
            // 歩
            Param(
                casePiece = Piece.Surface.Fu,
                casePosition = Position(5, 1),
                caseTurn = Turn.Normal.Black,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Fu,
                casePosition = Position(5, 2),
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Fu,
                casePosition = Position(5, 9),
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            // 香車
            Param(
                casePiece = Piece.Surface.Kyosya,
                casePosition = Position(5, 1),
                caseTurn = Turn.Normal.Black,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Kyosya,
                casePosition = Position(5, 2),
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Kyosya,
                casePosition = Position(5, 9),
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            // 桂馬
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(5, 2),
                caseTurn = Turn.Normal.Black,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(5, 3),
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(5, 8),
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            // 後手
            // 歩
            Param(
                casePiece = Piece.Surface.Fu,
                casePosition = Position(5, 9),
                caseTurn = Turn.Normal.White,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Fu,
                casePosition = Position(5, 8),
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Fu,
                casePosition = Position(5, 1),
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            // 香車
            Param(
                casePiece = Piece.Surface.Kyosya,
                casePosition = Position(5, 9),
                caseTurn = Turn.Normal.White,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Kyosya,
                casePosition = Position(5, 8),
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Kyosya,
                casePosition = Position(5, 1),
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            // 桂馬
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(5, 8),
                caseTurn = Turn.Normal.White,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(5, 7),
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(5, 2),
                caseTurn = Turn.Normal.White,
                result = false,
            )
        )

        params.forEach {
            val board = Board()
            it.casePiece
            val expected = it.casePiece.shouldEvolution(board, it.casePosition, it.caseTurn)
            Assert.assertEquals(expected, it.result)
        }
    }


    @Test
    fun `駒を打つことができるか`() {
        data class Param(
            val casePiece: Piece.Surface,
            val casePosition: Position,
            val caseTurn: Turn,
            val result: Boolean,
        )

        val params = listOf(
            // 先手
            // 歩
            Param(
                casePiece = Piece.Surface.Fu,
                casePosition = Position(5, 1),
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Fu,
                casePosition = Position(5, 2),
                caseTurn = Turn.Normal.Black,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Fu,
                casePosition = Position(5, 9),
                caseTurn = Turn.Normal.Black,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Fu,
                casePosition = Position(6, 4), // 歩が置いてある列
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Fu,
                casePosition = Position(4, 6), // 歩が置いてある行
                caseTurn = Turn.Normal.Black,
                result = true,
            ),
            // 香車
            Param(
                casePiece = Piece.Surface.Kyosya,
                casePosition = Position(5, 1),
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Kyosya,
                casePosition = Position(5, 2),
                caseTurn = Turn.Normal.Black,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Kyosya,
                casePosition = Position(5, 9),
                caseTurn = Turn.Normal.Black,
                result = true,
            ),
            // 桂馬
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(5, 2),
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(5, 3),
                caseTurn = Turn.Normal.Black,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(5, 8),
                caseTurn = Turn.Normal.Black,
                result = true,
            ),
            // 後手
            // 歩
            Param(
                casePiece = Piece.Surface.Fu,
                casePosition = Position(5, 9),
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Fu,
                casePosition = Position(5, 8),
                caseTurn = Turn.Normal.White,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Fu,
                casePosition = Position(5, 1),
                caseTurn = Turn.Normal.White,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Fu,
                casePosition = Position(6, 4), // 歩が置いてある列
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Fu,
                casePosition = Position(4, 6), // 歩が置いてある行
                caseTurn = Turn.Normal.White,
                result = true,
            ),
            // 香車
            Param(
                casePiece = Piece.Surface.Kyosya,
                casePosition = Position(5, 9),
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Kyosya,
                casePosition = Position(5, 8),
                caseTurn = Turn.Normal.White,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Kyosya,
                casePosition = Position(5, 1),
                caseTurn = Turn.Normal.White,
                result = true,
            ),
            // 桂馬
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(5, 8),
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(5, 7),
                caseTurn = Turn.Normal.White,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(5, 2),
                caseTurn = Turn.Normal.White,
                result = true,
            ),
        )

        params.forEach {
            val board = Board().apply {
                this.update(
                    Position(6, 6),
                    CellStatus.Fill.FromPiece(Piece.Surface.Fu, it.caseTurn)
                )
            }
            it.casePiece
            val expected = it.casePiece.isAvailablePut(board, it.casePosition, it.caseTurn)
            Assert.assertEquals(expected, it.result)
        }
    }
}