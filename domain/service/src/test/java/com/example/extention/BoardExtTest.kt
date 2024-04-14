package com.example.extention

import com.example.entity.game.board.Board
import com.example.entity.game.board.Cell
import com.example.entity.game.board.CellStatus
import com.example.entity.game.board.EvolutionCheckState
import com.example.entity.game.board.Position
import com.example.entity.game.piece.Piece
import com.example.entity.game.rule.GameRule
import com.example.entity.game.rule.Hande
import com.example.entity.game.rule.PlayersRule
import com.example.entity.game.rule.Turn
import com.example.test_entity.fake
import org.junit.Assert
import org.junit.Test

class BoardExtTest {

    @Test
    fun `盤面初期化`() {
        data class Param(
            val case: GameRule,
            val resultPieceList: Map<Position, Cell>,
        )

        fun Map<Position, CellStatus>.createBoard(): Map<Position, Cell> {
            return (0 until 9).flatMap { row ->
                (0 until 9).map { column ->
                    val position = Position(row + 1, column + 1)
                    val cellStatus = this[position] ?: CellStatus.Empty
                    val cell = Cell()
                    cell.update(cellStatus)
                    Pair(position, cell)
                }
            }.toMap()
        }

        val baseSet: Map<Position, CellStatus> = mutableMapOf(
            Position(5, 1) to CellStatus.Fill.FromPiece(Piece.Surface.Ou, Turn.Normal.White),
            Position(1, 3) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.White),
            Position(2, 3) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.White),
            Position(3, 3) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.White),
            Position(4, 3) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.White),
            Position(5, 3) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.White),
            Position(6, 3) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.White),
            Position(7, 3) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.White),
            Position(8, 3) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.White),
            Position(9, 3) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.White),
            Position(5, 9) to CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, Turn.Normal.Black),
            Position(1, 7) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black),
            Position(2, 7) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black),
            Position(3, 7) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black),
            Position(4, 7) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black),
            Position(5, 7) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black),
            Position(6, 7) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black),
            Position(7, 7) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black),
            Position(8, 7) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black),
            Position(9, 7) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black),
        )
            .plus(SetUpPiece.Normal.whiteKyo)
            .plus(SetUpPiece.Normal.whiteKei)
            .plus(SetUpPiece.Normal.whiteGin)
            .plus(SetUpPiece.Normal.whiteKin)
            .plus(SetUpPiece.Normal.whiteHisya)
            .plus(SetUpPiece.Normal.whiteKaku)
            .plus(SetUpPiece.Normal.blackKyo)
            .plus(SetUpPiece.Normal.blackKei)
            .plus(SetUpPiece.Normal.blackGin)
            .plus(SetUpPiece.Normal.blackKin)
            .plus(SetUpPiece.Normal.blackHisya)
            .plus(SetUpPiece.Normal.blackKaku)
        // data
        val params = listOf(
            Pair(
                PlayersRule.fake(blackHande = Hande.NON),
                baseSet
            ),
            Pair(
                PlayersRule.fake(blackHande = Hande.HISYA),
                baseSet.minus(SetUpPiece.Normal.blackHisya.first)
            ),
            Pair(
                PlayersRule.fake(blackHande = Hande.KAKU),
                baseSet.minus(SetUpPiece.Normal.blackKaku.first)
            ),
            Pair(
                PlayersRule.fake(blackHande = Hande.TWO),
                baseSet
                    .minus(SetUpPiece.Normal.blackHisya.first)
                    .minus(SetUpPiece.Normal.blackKaku.first)
            ),
            Pair(
                PlayersRule.fake(blackHande = Hande.FOR),
                baseSet
                    .minus(SetUpPiece.Normal.blackHisya.first)
                    .minus(SetUpPiece.Normal.blackKaku.first)
                    .minus(SetUpPiece.Normal.blackKyo.entries.map { it.key }.toSet())
            ),
            Pair(
                PlayersRule.fake(blackHande = Hande.SIX),
                baseSet
                    .minus(SetUpPiece.Normal.blackHisya.first)
                    .minus(SetUpPiece.Normal.blackKaku.first)
                    .minus(SetUpPiece.Normal.blackKyo.entries.map { it.key }.toSet())
                    .minus(SetUpPiece.Normal.blackKei.entries.map { it.key }.toSet())
            ),
            Pair(
                PlayersRule.fake(blackHande = Hande.EIGHT),
                baseSet
                    .minus(SetUpPiece.Normal.blackHisya.first)
                    .minus(SetUpPiece.Normal.blackKaku.first)
                    .minus(SetUpPiece.Normal.blackKyo.entries.map { it.key }.toSet())
                    .minus(SetUpPiece.Normal.blackKei.entries.map { it.key }.toSet())
                    .minus(SetUpPiece.Normal.blackGin.entries.map { it.key }.toSet())
            ),
            Pair(
                PlayersRule.fake(whiteHande = Hande.HISYA),
                baseSet.minus(SetUpPiece.Normal.whiteHisya.first)
            ),
            Pair(
                PlayersRule.fake(whiteHande = Hande.KAKU),
                baseSet.minus(SetUpPiece.Normal.whiteKaku.first)
            ),
            Pair(
                PlayersRule.fake(whiteHande = Hande.TWO),
                baseSet
                    .minus(SetUpPiece.Normal.whiteHisya.first)
                    .minus(SetUpPiece.Normal.whiteKaku.first)
            ),
            Pair(
                PlayersRule.fake(whiteHande = Hande.FOR),
                baseSet
                    .minus(SetUpPiece.Normal.whiteHisya.first)
                    .minus(SetUpPiece.Normal.whiteKaku.first)
                    .minus(SetUpPiece.Normal.whiteKyo.entries.map { it.key }.toSet())
            ),
            Pair(
                PlayersRule.fake(whiteHande = Hande.SIX),
                baseSet
                    .minus(SetUpPiece.Normal.whiteHisya.first)
                    .minus(SetUpPiece.Normal.whiteKaku.first)
                    .minus(SetUpPiece.Normal.whiteKyo.entries.map { it.key }.toSet())
                    .minus(SetUpPiece.Normal.whiteKei.entries.map { it.key }.toSet())
            ),
            Pair(
                PlayersRule.fake(whiteHande = Hande.EIGHT),
                baseSet
                    .minus(SetUpPiece.Normal.whiteHisya.first)
                    .minus(SetUpPiece.Normal.whiteKaku.first)
                    .minus(SetUpPiece.Normal.whiteKyo.entries.map { it.key }.toSet())
                    .minus(SetUpPiece.Normal.whiteKei.entries.map { it.key }.toSet())
                    .minus(SetUpPiece.Normal.whiteGin.entries.map { it.key }.toSet())
            ),
        )
            .map { (playerRule, baseSet) ->
                val gameRule = GameRule.fake(playersRule = playerRule)

                (0 until 9).flatMap { row ->
                    (0 until 9).map { column ->
                        val position = Position(row + 1, column + 1)
                        val cellStatus = baseSet[position] ?: CellStatus.Empty
                        val cell = Cell()
                        cell.update(cellStatus)
                        Pair(position, cell)
                    }
                }.toMap()
                Param(
                    case = gameRule,
                    resultPieceList = baseSet.createBoard(),
                )
            }

        // result
        params.forEach {
            val expected = Board.setUp(it.case)
            Assert.assertEquals(expected.getAllCells(), it.resultPieceList)
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
            val expected = board.searchMoveBy(position, it.caseTurn)
            Assert.assertEquals(expected.toSet(), it.resultPositions.toSet())
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
            val expected = board.searchMoveBy(position, Turn.Normal.Black)
            Assert.assertEquals(expected.toSet(), it.result.toSet())
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
            val expected = board.searchMoveBy(position, Turn.Normal.Black)
            Assert.assertEquals(expected.toSet(), it.result.toSet())
        }
    }

    @Test
    fun `持ち駒を打てる場所の判定`() {
        data class Param(
            val casePiece: Piece,
            val caseTurn: Turn,
            val result: Set<Position>,
        )

        // data
        val resultPositionList = (1..9).flatMap { row ->
            (1..9).map { column ->
                Position(row, column)
            }
        }.toMutableSet()
        val fuRow = 5
        val resultRow1PositionList = (1..9).map { row -> Position(row, 1) }.toSet()
        val resultRow2PositionList = (1..9).map { row -> Position(row, 2) }.toSet()
        val resultRow9PositionList = (1..9).map { row -> Position(row, 9) }.toSet()
        val resultRow8PositionList = (1..9).map { row -> Position(row, 8) }.toSet()
        val resultColumnFuPositionList = (1..9).map { column -> Position(fuRow, column) }.toSet()


        val params = listOf(
            // 制限のない駒
            Param(
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Ou,
                result = resultPositionList,
            ),
            Param(
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Gyoku,
                result = resultPositionList,
            ),
            Param(
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Kin,
                result = resultPositionList,
            ),
            Param(
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Gyoku,
                result = resultPositionList,
            ),
            Param(
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Hisya,
                result = resultPositionList,
            ),
            Param(
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Kaku,
                result = resultPositionList,
            ),
            Param(
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Reverse.Narigin,
                result = resultPositionList,
            ),
            Param(
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Reverse.Narikei,
                result = resultPositionList,
            ),
            Param(
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Reverse.Narikyo,
                result = resultPositionList,
            ),
            Param(
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Reverse.Uma,
                result = resultPositionList,
            ),
            Param(
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Reverse.Ryu,
                result = resultPositionList,
            ),
            Param(
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Reverse.To,
                result = resultPositionList,
            ),
            // 歩
            Param(
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Fu,
                result = resultPositionList - resultRow1PositionList - resultColumnFuPositionList,
            ),
            Param(
                caseTurn = Turn.Normal.White,
                casePiece = Piece.Surface.Fu,
                result = resultPositionList - resultRow9PositionList - resultColumnFuPositionList,
            ),
            // 香車
            Param(
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Kyosya,
                result = resultPositionList - resultRow1PositionList,
            ),
            Param(
                caseTurn = Turn.Normal.White,
                casePiece = Piece.Surface.Kyosya,
                result = resultPositionList - resultRow9PositionList,
            ),
            // 桂馬
            Param(
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Keima,
                result = resultPositionList - resultRow1PositionList - resultRow2PositionList,
            ),
            Param(
                caseTurn = Turn.Normal.White,
                casePiece = Piece.Surface.Keima,
                result = resultPositionList - resultRow9PositionList - resultRow8PositionList,
            ),
        )

        // result
        params.forEach {
            val board = Board().apply {
                if (it.casePiece == Piece.Surface.Fu) {
                    update(
                        Position(fuRow, 5),
                        CellStatus.Fill.FromPiece(Piece.Surface.Fu, it.caseTurn)
                    )
                }
            }
            val expected = board.searchPutBy(it.casePiece, it.caseTurn)
            Assert.assertEquals(expected.toSet(), it.result)
        }
    }

    @Test
    fun `駒が成れるか判定`() {
        data class Param(
            val caseBeforePosition: Position,
            val caseAfterPosition: Position,
            val caseTurn: Turn,
            val casePiece: Piece.Surface,
            val result: EvolutionCheckState,
        )

        // data
        val params = listOf(
            // 先手_縦１〜３以内での移動
            Param(
                caseBeforePosition = Position(1, 1),
                caseAfterPosition = Position(2, 3),
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Gin,
                result = EvolutionCheckState.Choose,
            ),
            // 先手_縦１〜３以外での移動
            Param(
                caseBeforePosition = Position(5, 5),
                caseAfterPosition = Position(5, 6),
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Gin,
                result = EvolutionCheckState.No,
            ),
            // 先手_縦１〜３以内→１〜３以外への移動
            Param(
                caseBeforePosition = Position(5, 3),
                caseAfterPosition = Position(5, 4),
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Gin,
                result = EvolutionCheckState.Choose,
            ),
            // 先手_縦１〜３以外→１〜３以内への移動
            Param(
                caseBeforePosition = Position(5, 4),
                caseAfterPosition = Position(5, 3),
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Gin,
                result = EvolutionCheckState.Choose,
            ),
            // 先手_範囲内だが裏がない駒の判定
            Param(
                caseBeforePosition = Position(5, 4),
                caseAfterPosition = Position(5, 3),
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Kin,
                result = EvolutionCheckState.No,
            ),
            // 先手_縦7 〜９以内での移動
            Param(
                caseBeforePosition = Position(5, 7),
                caseAfterPosition = Position(5, 8),
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Gin,
                result = EvolutionCheckState.No,
            ),
            // 先手_強制的にならなければならない場合（歩）
            Param(
                caseBeforePosition = Position(5, 2),
                caseAfterPosition = Position(5, 1),
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Fu,
                result = EvolutionCheckState.Should,
            ),
            // 先手_強制的にならなければならない場合（香車）
            Param(
                caseBeforePosition = Position(5, 2),
                caseAfterPosition = Position(5, 1),
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Kyosya,
                result = EvolutionCheckState.Should,
            ),
            // 先手_強制的にならなければならない場合（桂馬）
            Param(
                caseBeforePosition = Position(5, 4),
                caseAfterPosition = Position(4, 2),
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Keima,
                result = EvolutionCheckState.Should,
            ),
            // 後手_縦７〜９以内での移動
            Param(
                caseBeforePosition = Position(1, 7),
                caseAfterPosition = Position(2, 8),
                caseTurn = Turn.Normal.White,
                casePiece = Piece.Surface.Gin,
                result = EvolutionCheckState.Choose,
            ),
            // 後手_縦７〜８以外での移動
            Param(
                caseBeforePosition = Position(5, 5),
                caseAfterPosition = Position(5, 6),
                caseTurn = Turn.Normal.White,
                casePiece = Piece.Surface.Gin,
                result = EvolutionCheckState.No,
            ),
            // 後手_縦７〜９以内→７〜９以外への移動
            Param(
                caseBeforePosition = Position(5, 7),
                caseAfterPosition = Position(5, 6),
                caseTurn = Turn.Normal.White,
                casePiece = Piece.Surface.Gin,
                result = EvolutionCheckState.Choose,
            ),
            // 後手_縦７〜９以外→７〜９以内への移動
            Param(
                caseBeforePosition = Position(5, 6),
                caseAfterPosition = Position(5, 7),
                caseTurn = Turn.Normal.White,
                casePiece = Piece.Surface.Gin,
                result = EvolutionCheckState.Choose,
            ),
            // 後手_範囲内だが裏がない駒の判定
            Param(
                caseBeforePosition = Position(5, 7),
                caseAfterPosition = Position(5, 8),
                caseTurn = Turn.Normal.White,
                casePiece = Piece.Surface.Kin,
                result = EvolutionCheckState.No,
            ),
            // 後手_縦１〜３以内での移動
            Param(
                caseBeforePosition = Position(5, 1),
                caseAfterPosition = Position(5, 3),
                caseTurn = Turn.Normal.White,
                casePiece = Piece.Surface.Gin,
                result = EvolutionCheckState.No,
            ),
            // 強制的にならなければならない場合（歩）
            Param(
                caseBeforePosition = Position(5, 8),
                caseAfterPosition = Position(5, 9),
                caseTurn = Turn.Normal.White,
                casePiece = Piece.Surface.Fu,
                result = EvolutionCheckState.Should,
            ),
            // 強制的にならなければならない場合（香車）
            Param(
                caseBeforePosition = Position(5, 8),
                caseAfterPosition = Position(5, 9),
                caseTurn = Turn.Normal.White,
                casePiece = Piece.Surface.Kyosya,
                result = EvolutionCheckState.Should,
            ),
            // 強制的にならなければならない場合（桂馬）
            Param(
                caseBeforePosition = Position(5, 6),
                caseAfterPosition = Position(4, 8),
                caseTurn = Turn.Normal.White,
                casePiece = Piece.Surface.Keima,
                result = EvolutionCheckState.Should,
            ),
        )

        // result
        params.forEach {
            val board = Board().apply {
                update(it.caseBeforePosition, CellStatus.Fill.FromPiece(it.casePiece, it.caseTurn))
            }
            val expected = board.checkPieceEvolution(
                it.casePiece,
                it.caseBeforePosition,
                it.caseAfterPosition,
                it.caseTurn,
            )
            Assert.assertEquals(expected, it.result)
        }
    }

    @Test
    fun `王手の判定`() {
        data class Opponent(
            val position: Position,
        )

        data class Param(
            val casePiece: Piece,
            val casePosition: Position,
            val caseOpponent: Opponent?,
            val caseTurn: Turn,
            val result: Boolean,
        )


        val params = listOf(
            // 先手番
            // １マスずつで王手になっているマス
            Param(
                casePiece = Piece.Surface.Kin,
                casePosition = Position(4, 6),
                caseOpponent = null,
                caseTurn = Turn.Normal.Black,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Kin,
                casePosition = Position(5, 6),
                caseOpponent = null,
                caseTurn = Turn.Normal.Black,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Kin,
                casePosition = Position(6, 6),
                caseOpponent = null,
                caseTurn = Turn.Normal.Black,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Kin,
                casePosition = Position(4, 5),
                caseOpponent = null,
                caseTurn = Turn.Normal.Black,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Kin,
                casePosition = Position(6, 5),
                caseOpponent = null,
                caseTurn = Turn.Normal.Black,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Gin,
                casePosition = Position(4, 4),
                caseOpponent = null,
                caseTurn = Turn.Normal.Black,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Kin,
                casePosition = Position(5, 4),
                caseOpponent = null,
                caseTurn = Turn.Normal.Black,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Gin,
                casePosition = Position(4, 4),
                caseOpponent = null,
                caseTurn = Turn.Normal.Black,
                result = true,
            ),
            // １マスずつで王手になっていないマス
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(4, 6),
                caseOpponent = null,
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(5, 6),
                caseOpponent = null,
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(6, 6),
                caseOpponent = null,
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(4, 5),
                caseOpponent = null,
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(6, 5),
                caseOpponent = null,
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(4, 4),
                caseOpponent = null,
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(5, 4),
                caseOpponent = null,
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(4, 4),
                caseOpponent = null,
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            // 桂馬の動き
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(4, 7),
                caseOpponent = null,
                caseTurn = Turn.Normal.Black,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(6, 7),
                caseOpponent = null,
                caseTurn = Turn.Normal.Black,
                result = true,
            ),
            // 飛車角で途中で防がれている時
            Param(
                casePiece = Piece.Surface.Hisya,
                casePosition = Position(5, 9),
                caseOpponent = Opponent(
                    position = Position(5, 7),
                ),
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Hisya,
                casePosition = Position(5, 1),
                caseOpponent = Opponent(
                    position = Position(5, 3),
                ),
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Hisya,
                casePosition = Position(9, 5),
                caseOpponent = Opponent(
                    position = Position(7, 5),
                ),
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Hisya,
                casePosition = Position(1, 5),
                caseOpponent = Opponent(
                    position = Position(3, 5),
                ),
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Hisya,
                casePosition = Position(5, 9),
                caseOpponent = Opponent(
                    position = Position(5, 7),
                ),
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Kaku,
                casePosition = Position(1, 1),
                caseOpponent = Opponent(
                    position = Position(3, 3),
                ),
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Kaku,
                casePosition = Position(9, 1),
                caseOpponent = Opponent(
                    position = Position(7, 3),
                ),
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Kaku,
                casePosition = Position(1, 9),
                caseOpponent = Opponent(
                    position = Position(3, 7),
                ),
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            // 後手番
            // １マスずつで王手になっているマス
            Param(
                casePiece = Piece.Surface.Gin,
                casePosition = Position(4, 6),
                caseOpponent = null,
                caseTurn = Turn.Normal.White,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Kin,
                casePosition = Position(5, 6),
                caseOpponent = null,
                caseTurn = Turn.Normal.White,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Gin,
                casePosition = Position(6, 6),
                caseOpponent = null,
                caseTurn = Turn.Normal.White,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Kin,
                casePosition = Position(4, 5),
                caseOpponent = null,
                caseTurn = Turn.Normal.White,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Kin,
                casePosition = Position(6, 5),
                caseOpponent = null,
                caseTurn = Turn.Normal.White,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Kin,
                casePosition = Position(4, 4),
                caseOpponent = null,
                caseTurn = Turn.Normal.White,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Kin,
                casePosition = Position(5, 4),
                caseOpponent = null,
                caseTurn = Turn.Normal.White,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Kin,
                casePosition = Position(4, 4),
                caseOpponent = null,
                caseTurn = Turn.Normal.White,
                result = true,
            ),
            // １マスずつで王手になっていないマス
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(4, 6),
                caseOpponent = null,
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(5, 6),
                caseOpponent = null,
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(6, 6),
                caseOpponent = null,
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(4, 5),
                caseOpponent = null,
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(6, 5),
                caseOpponent = null,
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(4, 4),
                caseOpponent = null,
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(5, 4),
                caseOpponent = null,
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(4, 4),
                caseOpponent = null,
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            // 桂馬の動き
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(4, 3),
                caseOpponent = null,
                caseTurn = Turn.Normal.White,
                result = true,
            ),
            Param(
                casePiece = Piece.Surface.Keima,
                casePosition = Position(6, 3),
                caseOpponent = null,
                caseTurn = Turn.Normal.White,
                result = true,
            ),
            // 飛車角で途中で防がれている時
            Param(
                casePiece = Piece.Surface.Hisya,
                casePosition = Position(5, 9),
                caseOpponent = Opponent(
                    position = Position(5, 7),
                ),
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Hisya,
                casePosition = Position(5, 1),
                caseOpponent = Opponent(
                    position = Position(5, 3),
                ),
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Hisya,
                casePosition = Position(9, 5),
                caseOpponent = Opponent(
                    position = Position(7, 5),
                ),
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Hisya,
                casePosition = Position(1, 5),
                caseOpponent = Opponent(
                    position = Position(3, 5),
                ),
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Hisya,
                casePosition = Position(5, 9),
                caseOpponent = Opponent(
                    position = Position(5, 7),
                ),
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Kaku,
                casePosition = Position(1, 1),
                caseOpponent = Opponent(
                    position = Position(3, 3),
                ),
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Kaku,
                casePosition = Position(9, 1),
                caseOpponent = Opponent(
                    position = Position(7, 3),
                ),
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            Param(
                casePiece = Piece.Surface.Kaku,
                casePosition = Position(1, 9),
                caseOpponent = Opponent(
                    position = Position(3, 7),
                ),
                caseTurn = Turn.Normal.White,
                result = false,
            ),
        )

        // result
        params.forEach {
            val opponentTun = when (it.caseTurn) {
                Turn.Normal.Black -> Turn.Normal.White
                Turn.Normal.White -> Turn.Normal.Black
            }
            val board = Board().apply {

                val king = when (opponentTun) {
                    Turn.Normal.Black -> Piece.Surface.Gyoku
                    Turn.Normal.White -> Piece.Surface.Ou
                }

                update(Position(5, 5), CellStatus.Fill.FromPiece(king, opponentTun))
                update(it.casePosition, CellStatus.Fill.FromPiece(it.casePiece, it.caseTurn))
                val opponent = it.caseOpponent ?: return@apply
                update(opponent.position, CellStatus.Fill.FromPiece(Piece.Surface.Fu, opponentTun))
            }
            val expected = board.isCheckByTurn(opponentTun)
            Assert.assertEquals(expected, it.result)
        }
    }

    @Test
    fun `指定したマスの駒を成らせる`() {
        data class Param(
            val casePiece: Piece,
            val result: Piece,
        )

        val params = listOf(
            // 成ることができる駒
            Param(
                casePiece = Piece.Surface.Fu,
                result = Piece.Reverse.To,
            ),
            // 成ることができない駒
            Param(
                casePiece = Piece.Surface.Kin,
                result = Piece.Surface.Kin,
            ),
            // すでに成るっている駒
            Param(
                casePiece = Piece.Reverse.To,
                result = Piece.Reverse.To,
            ),
        )

        params.forEach {
            val position = Position(5, 3)
            val board = Board().apply {
                val cellStatus = CellStatus.Fill.FromPiece(it.casePiece, Turn.Normal.Black)
                this.update(position, cellStatus)
            }
            val resultBoard = board.copy()
            val expected = board.updatePieceEvolution(position)
            val cellStatus = CellStatus.Fill.FromPiece(it.result, Turn.Normal.Black)
            resultBoard.update(position, cellStatus)
            Assert.assertEquals(expected, resultBoard)
        }
    }
}
