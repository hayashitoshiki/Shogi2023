package com.example.service

import com.example.entity.game.board.Board
import com.example.entity.game.board.Cell
import com.example.entity.game.board.CellStatus
import com.example.entity.game.board.Position
import com.example.entity.game.board.Size
import com.example.entity.game.piece.Piece
import com.example.entity.game.rule.PieceSetUpRule
import com.example.entity.game.rule.Turn
import org.junit.Assert.assertEquals
import org.junit.Test

class GameServiceTest {

    private val gameService = GameService()

    @Test
    fun `盤面初期化`() {
        data class Param(
            val case: PieceSetUpRule,
            val resultBoardSize: Size,
            val resultPieceList: Map<Position, Cell>,
        )

        fun PieceSetUpRule.createBoard(): Map<Position, Cell> {
            return (0 until this.boardSize.row).flatMap { row ->
                (0 until this.boardSize.column).map { column ->
                    val position = Position(row + 1, column + 1)
                    val cellStatus = this.initPiece[position] ?: CellStatus.Empty
                    val cell = Cell()
                    cell.update(cellStatus)
                    Pair(position, cell)
                }
            }.toMap()
        }

        // data
        val params = listOf(
            PieceSetUpRule.Normal.NoHande,
            PieceSetUpRule.Normal.BlackHandeHisya,
            PieceSetUpRule.Normal.BlackHandeKaku,
            PieceSetUpRule.Normal.BlackHandeHisyaKaku,
            PieceSetUpRule.Normal.BlackHandeFor,
            PieceSetUpRule.Normal.BlackHandeSix,
            PieceSetUpRule.Normal.BlackHandeEight,
            PieceSetUpRule.Normal.WhiteHandeHisya,
            PieceSetUpRule.Normal.WhiteHandeKaku,
            PieceSetUpRule.Normal.WhiteHandeHisyaKaku,
            PieceSetUpRule.Normal.WhiteHandeFor,
            PieceSetUpRule.Normal.WhiteHandeSix,
            PieceSetUpRule.Normal.WhiteHandeEight,
        )
            .map { rule ->
                Param(
                    case = rule,
                    resultBoardSize = rule.boardSize,
                    resultPieceList = rule.createBoard(),
                )
            }

        // result
        params.forEach {
            val expected = gameService.setUpBoard(it.case)
            assertEquals(expected.size, it.resultBoardSize)
            assertEquals(expected.getAllCells(), it.resultPieceList)
        }
    }


    @Test
    fun `盤上の駒を動かす_各種駒の動き`() {
        data class Param(
            val casePiece: Piece,
            val caseTurn: Turn,
            val resultPositions: List<Position>,
        )

        // data
        val resultPositionsOu = listOf(
            Position(4, 4),
            Position(5, 4),
            Position(6, 4),
            Position(4, 5),
            Position(6, 5),
            Position(4, 6),
            Position(5, 6),
            Position(6, 6),
        )
        val resultPositionsKinBlack = listOf(
            Position(4, 4),
            Position(5, 4),
            Position(6, 4),
            Position(4, 5),
            Position(6, 5),
            Position(5, 6),
        )
        val resultPositionsKinWhite = listOf(
            Position(4, 6),
            Position(5, 6),
            Position(6, 6),
            Position(4, 5),
            Position(6, 5),
            Position(5, 4),
        )
        val resultPositionsHisya = listOf(
            Position(5, 1),
            Position(5, 2),
            Position(5, 3),
            Position(5, 4),
            Position(5, 6),
            Position(5, 7),
            Position(5, 8),
            Position(5, 9),
            Position(1, 5),
            Position(2, 5),
            Position(3, 5),
            Position(4, 5),
            Position(6, 5),
            Position(7, 5),
            Position(8, 5),
            Position(9, 5),
        )
        val resultPositionsKaku = listOf(
            Position(1, 1),
            Position(2, 2),
            Position(3, 3),
            Position(4, 4),
            Position(6, 6),
            Position(7, 7),
            Position(8, 8),
            Position(9, 9),
            Position(1, 9),
            Position(2, 8),
            Position(3, 7),
            Position(4, 6),
            Position(6, 4),
            Position(7, 3),
            Position(8, 2),
            Position(9, 1),
        )
        val params = listOf(
            Param(
                casePiece = Piece.Surface.Fu,
                caseTurn = Turn.Normal.Black,
                resultPositions = listOf(
                    Position(5, 4),
                ),
            ),
            Param(
                casePiece = Piece.Surface.Fu,
                caseTurn = Turn.Normal.White,
                resultPositions = listOf(
                    Position(5, 6),
                ),
            ),
            Param(
                casePiece = Piece.Surface.Kyosya,
                caseTurn = Turn.Normal.Black,
                resultPositions = listOf(
                    Position(5, 4),
                    Position(5, 3),
                    Position(5, 2),
                    Position(5, 1),
                ),
            ),
            Param(
                casePiece = Piece.Surface.Kyosya,
                caseTurn = Turn.Normal.White,
                resultPositions = listOf(
                    Position(5, 6),
                    Position(5, 7),
                    Position(5, 8),
                    Position(5, 9),
                ),
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                caseTurn = Turn.Normal.Black,
                resultPositions = listOf(
                    Position(4, 3),
                    Position(6, 3),
                ),
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                caseTurn = Turn.Normal.White,
                resultPositions = listOf(
                    Position(6, 7),
                    Position(4, 7),
                ),
            ),
            Param(
                casePiece = Piece.Surface.Gin,
                caseTurn = Turn.Normal.Black,
                resultPositions = listOf(
                    Position(4, 4),
                    Position(5, 4),
                    Position(6, 4),
                    Position(4, 6),
                    Position(6, 6),
                ),
            ),
            Param(
                casePiece = Piece.Surface.Gin,
                caseTurn = Turn.Normal.White,
                resultPositions = listOf(
                    Position(4, 6),
                    Position(5, 6),
                    Position(6, 6),
                    Position(4, 4),
                    Position(6, 4),
                ),
            ),
            Param(
                casePiece = Piece.Surface.Kin,
                caseTurn = Turn.Normal.Black,
                resultPositions = listOf(
                    Position(4, 4),
                    Position(5, 4),
                    Position(6, 4),
                    Position(4, 5),
                    Position(6, 5),
                    Position(5, 6),
                ),
            ),
            Param(
                casePiece = Piece.Surface.Kin,
                caseTurn = Turn.Normal.White,
                resultPositions = listOf(
                    Position(4, 6),
                    Position(5, 6),
                    Position(6, 6),
                    Position(4, 5),
                    Position(6, 5),
                    Position(5, 4),
                ),
            ),
            Param(
                casePiece = Piece.Surface.Gyoku,
                caseTurn = Turn.Normal.Black,
                resultPositions = resultPositionsOu,
            ),
            Param(
                casePiece = Piece.Surface.Ou,
                caseTurn = Turn.Normal.White,
                resultPositions = resultPositionsOu,
            ),
            Param(
                casePiece = Piece.Surface.Hisya,
                caseTurn = Turn.Normal.Black,
                resultPositions = resultPositionsHisya,
            ),
            Param(
                casePiece = Piece.Surface.Hisya,
                caseTurn = Turn.Normal.White,
                resultPositions = resultPositionsHisya,
            ),
            Param(
                casePiece = Piece.Surface.Kaku,
                caseTurn = Turn.Normal.Black,
                resultPositions = resultPositionsKaku,
            ),
            Param(
                casePiece = Piece.Surface.Kaku,
                caseTurn = Turn.Normal.White,
                resultPositions = resultPositionsKaku,
            ),
            Param(
                casePiece = Piece.Reverse.To,
                caseTurn = Turn.Normal.Black,
                resultPositions = resultPositionsKinBlack,
            ),
            Param(
                casePiece = Piece.Reverse.To,
                caseTurn = Turn.Normal.White,
                resultPositions = resultPositionsKinWhite,
            ),
            Param(
                casePiece = Piece.Reverse.Narikyo,
                caseTurn = Turn.Normal.Black,
                resultPositions = resultPositionsKinBlack,
            ),
            Param(
                casePiece = Piece.Reverse.Narikyo,
                caseTurn = Turn.Normal.White,
                resultPositions = resultPositionsKinWhite,
            ),
            Param(
                casePiece = Piece.Reverse.Narikei,
                caseTurn = Turn.Normal.Black,
                resultPositions = resultPositionsKinBlack,
            ),
            Param(
                casePiece = Piece.Reverse.Narikei,
                caseTurn = Turn.Normal.White,
                resultPositions = resultPositionsKinWhite,
            ),
            Param(
                casePiece = Piece.Reverse.Narigin,
                caseTurn = Turn.Normal.Black,
                resultPositions = resultPositionsKinBlack,
            ),
            Param(
                casePiece = Piece.Reverse.Narigin,
                caseTurn = Turn.Normal.White,
                resultPositions = resultPositionsKinWhite,
            ),
            Param(
                casePiece = Piece.Reverse.Uma,
                caseTurn = Turn.Normal.Black,
                resultPositions = resultPositionsKaku + resultPositionsOu,
            ),
            Param(
                casePiece = Piece.Reverse.Uma,
                caseTurn = Turn.Normal.White,
                resultPositions = resultPositionsKaku + resultPositionsOu,
            ),
            Param(
                casePiece = Piece.Reverse.Ryu,
                caseTurn = Turn.Normal.Black,
                resultPositions = resultPositionsHisya + resultPositionsOu,
            ),
            Param(
                casePiece = Piece.Reverse.Ryu,
                caseTurn = Turn.Normal.White,
                resultPositions = resultPositionsHisya + resultPositionsOu,
            ),
        )

        // result
        params.forEach {
            val board = Board()
            val position = Position(5, 5)
            board.update(position, CellStatus.Fill.FromPiece(it.casePiece, it.caseTurn))
            val expected = gameService.searchMoveBy(board, position, it.caseTurn)
            assertEquals(expected.toSet(), it.resultPositions.toSet())
        }
    }

    @Test
    fun `盤上の駒を動かす_自分のコマの上の場合`() {
        data class Param(
            val casePosition: Position,
            val casePiece: Piece,
            val result: List<Position>,
        )

        val resultPositionsOu = listOf(
            Position(4, 4),
            Position(5, 4),
            Position(6, 4),
            Position(4, 5),
            Position(6, 5),
            Position(4, 6),
            Position(5, 6),
            Position(6, 6),
        )
        val resultPositionsHisya = listOf(
            Position(5, 1),
            Position(5, 2),
            Position(5, 3),
            Position(5, 4),
            Position(5, 6),
            Position(5, 7),
            Position(5, 8),
            Position(5, 9),
            Position(1, 5),
            Position(2, 5),
            Position(3, 5),
            Position(4, 5),
            Position(6, 5),
            Position(7, 5),
            Position(8, 5),
            Position(9, 5),
        )
        val resultPositionsKaku = listOf(
            Position(1, 1),
            Position(2, 2),
            Position(3, 3),
            Position(4, 4),
            Position(6, 6),
            Position(7, 7),
            Position(8, 8),
            Position(9, 9),
            Position(1, 9),
            Position(2, 8),
            Position(3, 7),
            Position(4, 6),
            Position(6, 4),
            Position(7, 3),
            Position(8, 2),
            Position(9, 1),
        )
        // 飛車角系で途中に自分の駒がある場合
        val params = mutableListOf(
            // 飛車
            Param(
                casePosition = Position(5, 3),
                casePiece = Piece.Surface.Hisya,
                result = resultPositionsHisya.toMutableList().apply {
                    remove(Position(5, 3))
                    remove(Position(5, 2))
                    remove(Position(5, 1))
                },
            ),
            Param(
                casePosition = Position(5, 7),
                casePiece = Piece.Surface.Hisya,
                result = resultPositionsHisya.toMutableList().apply {
                    remove(Position(5, 7))
                    remove(Position(5, 8))
                    remove(Position(5, 9))
                },
            ),
            Param(
                casePosition = Position(3, 5),
                casePiece = Piece.Surface.Hisya,
                result = resultPositionsHisya.toMutableList().apply {
                    remove(Position(3, 5))
                    remove(Position(2, 5))
                    remove(Position(1, 5))
                },
            ),
            Param(
                casePosition = Position(7, 5),
                casePiece = Piece.Surface.Hisya,
                result = resultPositionsHisya.toMutableList().apply {
                    remove(Position(7, 5))
                    remove(Position(8, 5))
                    remove(Position(9, 5))
                },
            ),
            // 角
            Param(
                casePosition = Position(3, 3),
                casePiece = Piece.Surface.Kaku,
                result = resultPositionsKaku.toMutableList().apply {
                    remove(Position(3, 3))
                    remove(Position(2, 2))
                    remove(Position(1, 1))
                },
            ),
            Param(
                casePosition = Position(7, 7),
                casePiece = Piece.Surface.Kaku,
                result = resultPositionsKaku.toMutableList().apply {
                    remove(Position(7, 7))
                    remove(Position(8, 8))
                    remove(Position(9, 9))
                },
            ),
            Param(
                casePosition = Position(3, 7),
                casePiece = Piece.Surface.Kaku,
                result = resultPositionsKaku.toMutableList().apply {
                    remove(Position(3, 7))
                    remove(Position(2, 8))
                    remove(Position(1, 9))
                },
            ),
            Param(
                casePosition = Position(7, 3),
                casePiece = Piece.Surface.Kaku,
                result = resultPositionsKaku.toMutableList().apply {
                    remove(Position(7, 3))
                    remove(Position(8, 2))
                    remove(Position(9, 1))
                },
            ),
        )
        // 王（周囲一ます）
        resultPositionsOu.forEach {
            params.add(
                Param(
                    casePosition = it,
                    casePiece = Piece.Surface.Ou,
                    result = resultPositionsOu.toMutableList().apply { remove(it) },
                ),
            )
        }

        // result
        params.forEach {
            val board = Board()
            val position = Position(5, 5)
            board.update(position, CellStatus.Fill.FromPiece(it.casePiece, Turn.Normal.Black))
            board.update(
                it.casePosition,
                CellStatus.Fill.FromPiece(Piece.Surface.Keima, Turn.Normal.Black)
            )
            val expected = gameService.searchMoveBy(board, position, Turn.Normal.Black)
            assertEquals(expected.toSet(), it.result.toSet())
        }
    }

    @Test
    fun `盤上の駒を動かす_敵の駒の上の場合`() {
        data class Param(
            val casePosition: Position,
            val casePiece: Piece,
            val result: List<Position>,
        )

        val resultPositionsOu = listOf(
            Position(4, 4),
            Position(5, 4),
            Position(6, 4),
            Position(4, 5),
            Position(6, 5),
            Position(4, 6),
            Position(5, 6),
            Position(6, 6),
        )
        val resultPositionsHisya = listOf(
            Position(5, 1),
            Position(5, 2),
            Position(5, 3),
            Position(5, 4),
            Position(5, 6),
            Position(5, 7),
            Position(5, 8),
            Position(5, 9),
            Position(1, 5),
            Position(2, 5),
            Position(3, 5),
            Position(4, 5),
            Position(6, 5),
            Position(7, 5),
            Position(8, 5),
            Position(9, 5),
        )
        val resultPositionsKaku = listOf(
            Position(1, 1),
            Position(2, 2),
            Position(3, 3),
            Position(4, 4),
            Position(6, 6),
            Position(7, 7),
            Position(8, 8),
            Position(9, 9),
            Position(1, 9),
            Position(2, 8),
            Position(3, 7),
            Position(4, 6),
            Position(6, 4),
            Position(7, 3),
            Position(8, 2),
            Position(9, 1),
        )
        // 飛車角系で途中に自分の駒がある場合
        val params = mutableListOf(
            // 飛車
            Param(
                casePosition = Position(5, 3),
                casePiece = Piece.Surface.Hisya,
                result = resultPositionsHisya.toMutableList().apply {
                    remove(Position(5, 2))
                    remove(Position(5, 1))
                },
            ),
            Param(
                casePosition = Position(5, 7),
                casePiece = Piece.Surface.Hisya,
                result = resultPositionsHisya.toMutableList().apply {
                    remove(Position(5, 8))
                    remove(Position(5, 9))
                },
            ),
            Param(
                casePosition = Position(3, 5),
                casePiece = Piece.Surface.Hisya,
                result = resultPositionsHisya.toMutableList().apply {
                    remove(Position(2, 5))
                    remove(Position(1, 5))
                },
            ),
            Param(
                casePosition = Position(7, 5),
                casePiece = Piece.Surface.Hisya,
                result = resultPositionsHisya.toMutableList().apply {
                    remove(Position(8, 5))
                    remove(Position(9, 5))
                },
            ),
            // 角
            Param(
                casePosition = Position(3, 3),
                casePiece = Piece.Surface.Kaku,
                result = resultPositionsKaku.toMutableList().apply {
                    remove(Position(2, 2))
                    remove(Position(1, 1))
                },
            ),
            Param(
                casePosition = Position(7, 7),
                casePiece = Piece.Surface.Kaku,
                result = resultPositionsKaku.toMutableList().apply {
                    remove(Position(8, 8))
                    remove(Position(9, 9))
                },
            ),
            Param(
                casePosition = Position(3, 7),
                casePiece = Piece.Surface.Kaku,
                result = resultPositionsKaku.toMutableList().apply {
                    remove(Position(2, 8))
                    remove(Position(1, 9))
                },
            ),
            Param(
                casePosition = Position(7, 3),
                casePiece = Piece.Surface.Kaku,
                result = resultPositionsKaku.toMutableList().apply {
                    remove(Position(8, 2))
                    remove(Position(9, 1))
                },
            ),
        )
        // 王（周囲一ます）
        resultPositionsOu.forEach {
            params.add(
                Param(
                    casePosition = it,
                    casePiece = Piece.Surface.Ou,
                    result = resultPositionsOu,
                ),
            )
        }

        // result
        params.forEach {
            val board = Board()
            val position = Position(5, 5)
            board.update(position, CellStatus.Fill.FromPiece(it.casePiece, Turn.Normal.Black))
            board.update(
                it.casePosition,
                CellStatus.Fill.FromPiece(Piece.Surface.Keima, Turn.Normal.White)
            )
            val expected = gameService.searchMoveBy(board, position, Turn.Normal.Black)
            assertEquals(expected.toSet(), it.result.toSet())
        }
    }
}