package com.example.service

import com.example.entity.game.board.Board
import com.example.entity.game.board.Cell
import com.example.entity.game.board.CellStatus
import com.example.entity.game.board.Position
import com.example.entity.game.board.Size
import com.example.entity.game.board.Stand
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

    @Test
    fun `盤上の駒を動かす`() {
        data class Param(
            val case: CellStatus,
            val resultBoard: Board,
            val resultStand: Stand,
        )

        // data
        val stand = Stand()
        val position1 = Position(5, 4)
        val resultBoard = Board().apply {
            update(position1, CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black))
        }
        val standAddFu = Stand().apply { add(Piece.Surface.Fu) }
        val params = listOf(
            // 空のマス目に移動
            Param(
                case = CellStatus.Empty,
                resultBoard = resultBoard,
                resultStand = stand,
            ),
            // 相手の駒のマスに移動
            Param(
                case = CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.White),
                resultBoard = resultBoard,
                resultStand = standAddFu,
            ),
            // 相手の成り駒のマスに移動
            Param(
                case = CellStatus.Fill.FromPiece(Piece.Reverse.To, Turn.Normal.White),
                resultBoard = resultBoard,
                resultStand = standAddFu,
            )
        )

        // result
        params.forEach {
            val position2 = Position(5, 5)
            stand.clear()
            val board = Board().apply {
                update(position2, CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black))
                update(position1, it.case)
            }
            val expected = gameService.movePieceByPosition(board, stand, position2, position1)
            assertEquals(
                expected.first.getAllCells().toList().toSet(),
                it.resultBoard.getAllCells().toList().toSet()
            )
            assertEquals(expected.second.pieces, it.resultStand.pieces)
        }
    }

    @Test
    fun `駒が成れるか判定`() {
        data class Param(
            val caseBeforePosition: Position,
            val caseAfterPosition: Position,
            val caseTurn: Turn,
            val casePiece: Piece,
            val result: Boolean,
        )

        // data
        val params = listOf(
            // 先手_縦１〜３以内での移動
            Param(
                caseBeforePosition = Position(1, 1),
                caseAfterPosition = Position(2, 3),
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Gin,
                result = true,
            ),
            // 先手_縦１〜３以外での移動
            Param(
                caseBeforePosition = Position(5, 5),
                caseAfterPosition = Position(5, 6),
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Gin,
                result = false,
            ),
            // 先手_縦１〜３以内→１〜３以外への移動
            Param(
                caseBeforePosition = Position(5, 3),
                caseAfterPosition = Position(5, 4),
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Gin,
                result = true,
            ),
            // 先手_縦１〜３以外→１〜３以内への移動
            Param(
                caseBeforePosition = Position(5, 4),
                caseAfterPosition = Position(5, 3),
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Gin,
                result = true,
            ),
            // 先手_範囲内だが裏がない駒の判定
            Param(
                caseBeforePosition = Position(5, 4),
                caseAfterPosition = Position(5, 3),
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Kin,
                result = false,
            ),
            // 先手_縦7 〜９以内での移動
            Param(
                caseBeforePosition = Position(5, 7),
                caseAfterPosition = Position(5, 8),
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Surface.Gin,
                result = false,
            ),
            // 先手_範囲内だが既に成っている駒の判定
            Param(
                caseBeforePosition = Position(5, 4),
                caseAfterPosition = Position(5, 3),
                caseTurn = Turn.Normal.Black,
                casePiece = Piece.Reverse.Narigin,
                result = false,
            ),
            // 後手_縦７〜９以内での移動
            Param(
                caseBeforePosition = Position(1, 7),
                caseAfterPosition = Position(2, 8),
                caseTurn = Turn.Normal.White,
                casePiece = Piece.Surface.Gin,
                result = true,
            ),
            // 後手_縦７〜８以外での移動
            Param(
                caseBeforePosition = Position(5, 5),
                caseAfterPosition = Position(5, 6),
                caseTurn = Turn.Normal.White,
                casePiece = Piece.Surface.Gin,
                result = false,
            ),
            // 後手_縦７〜９以内→７〜９以外への移動
            Param(
                caseBeforePosition = Position(5, 7),
                caseAfterPosition = Position(5, 6),
                caseTurn = Turn.Normal.White,
                casePiece = Piece.Surface.Gin,
                result = true,
            ),
            // 後手_縦７〜９以外→７〜９以内への移動
            Param(
                caseBeforePosition = Position(5, 6),
                caseAfterPosition = Position(5, 7),
                caseTurn = Turn.Normal.White,
                casePiece = Piece.Surface.Gin,
                result = true,
            ),
            // 後手_範囲内だが裏がない駒の判定
            Param(
                caseBeforePosition = Position(5, 7),
                caseAfterPosition = Position(5, 8),
                caseTurn = Turn.Normal.White,
                casePiece = Piece.Surface.Kin,
                result = false,
            ),
            // 後手_縦１〜３以内での移動
            Param(
                caseBeforePosition = Position(5, 1),
                caseAfterPosition = Position(5, 3),
                caseTurn = Turn.Normal.White,
                casePiece = Piece.Surface.Gin,
                result = false,
            ),
            // 後手_範囲内だが既に成っている駒の判定
            Param(
                caseBeforePosition = Position(5, 7),
                caseAfterPosition = Position(5, 8),
                caseTurn = Turn.Normal.White,
                casePiece = Piece.Reverse.Narigin,
                result = false,
            ),
        )

        // result
        params.forEach {
            val board = Board().apply {
                update(it.caseBeforePosition, CellStatus.Fill.FromPiece(it.casePiece, it.caseTurn))
            }
            val expected = gameService.checkPieceEvolution(
                board,
                it.caseBeforePosition,
                it.caseAfterPosition
            )
            assertEquals(expected, it.result)
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
            val expected = gameService.searchPutBy(board, it.casePiece, it.caseTurn)
            assertEquals(expected.toSet(), it.result)
        }
    }
}