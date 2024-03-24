package com.example.service

import com.example.entity.game.board.Board
import com.example.entity.game.board.CellStatus
import com.example.entity.game.board.Position
import com.example.entity.game.board.Stand
import com.example.entity.game.piece.Piece
import com.example.entity.game.rule.BoardRule
import com.example.entity.game.rule.GameRule
import com.example.entity.game.rule.Turn
import com.example.entity.game.rule.UserRule
import com.example.entity.game.rule.UsersRule
import org.junit.Assert.assertEquals
import org.junit.Test

class GameServiceTest {

    private val gameService = GameService()

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
    fun `詰み判定`() {
        data class Param(
            val caseCellPattern: List<Pair<Position, CellStatus>>,
            val caseUseStandPiece: Boolean,
            val caseTurn: Turn,
            val result: Boolean,
        )

        val params = listOf(
            // 先手
            // 逃げ場なし & 取れる駒なし & 動かして防げる駒なし & 打って防げる状態なし
            Param(
                caseCellPattern = listOf(
                    Pair(
                        Position(5, 9),
                        CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, Turn.Normal.Black)
                    ),
                    Pair(
                        Position(5, 8),
                        CellStatus.Fill.FromPiece(Piece.Surface.Kin, Turn.Normal.White)
                    ),
                    Pair(
                        Position(5, 7),
                        CellStatus.Fill.FromPiece(Piece.Surface.Kin, Turn.Normal.White)
                    ),
                ),
                caseUseStandPiece = true,
                caseTurn = Turn.Normal.Black,
                result = true,
            ),
            // 逃げ場あり & 取れる駒なし & 動かして防げる駒なし & 打って防げる状態なし
            Param(
                caseCellPattern = listOf(
                    Pair(
                        Position(5, 8),
                        CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, Turn.Normal.Black)
                    ),
                    Pair(
                        Position(5, 7),
                        CellStatus.Fill.FromPiece(Piece.Surface.Kin, Turn.Normal.White)
                    ),
                    Pair(
                        Position(5, 6),
                        CellStatus.Fill.FromPiece(Piece.Surface.Kin, Turn.Normal.White)
                    ),
                ),
                caseUseStandPiece = true,
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            // 逃げ場なし & 取れる駒あり & 動かして防げる駒なし & 打って防げる状態なし
            Param(
                caseCellPattern = listOf(
                    Pair(
                        Position(5, 9),
                        CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, Turn.Normal.Black)
                    ),
                    Pair(
                        Position(5, 8),
                        CellStatus.Fill.FromPiece(Piece.Surface.Kin, Turn.Normal.White)
                    ),
                    Pair(
                        Position(5, 7),
                        CellStatus.Fill.FromPiece(Piece.Surface.Kin, Turn.Normal.White)
                    ),
                    Pair(
                        Position(4, 8),
                        CellStatus.Fill.FromPiece(Piece.Surface.Hisya, Turn.Normal.Black)
                    ),
                ),
                caseUseStandPiece = true,
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            // 逃げ場なし & 取れる駒なし & 動かして防げる駒あり & 打って防げる状態なし
            Param(
                caseCellPattern = listOf(
                    Pair(
                        Position(1, 9),
                        CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, Turn.Normal.Black)
                    ),
                    Pair(
                        Position(2, 9),
                        CellStatus.Fill.FromPiece(Piece.Surface.Keima, Turn.Normal.Black)
                    ),
                    Pair(
                        Position(2, 8),
                        CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black)
                    ),
                    Pair(
                        Position(1, 1),
                        CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, Turn.Normal.White)
                    ),
                ),
                caseUseStandPiece = false,
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            // 逃げ場なし & 取れる駒なし & 動かして防げる駒なし & 打って防げる状態あり
            Param(
                caseCellPattern = listOf(
                    Pair(
                        Position(1, 9),
                        CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, Turn.Normal.Black)
                    ),
                    Pair(
                        Position(2, 9),
                        CellStatus.Fill.FromPiece(Piece.Surface.Keima, Turn.Normal.Black)
                    ),
                    Pair(
                        Position(2, 8),
                        CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, Turn.Normal.Black)
                    ),
                    Pair(
                        Position(1, 1),
                        CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, Turn.Normal.White)
                    ),
                ),
                caseUseStandPiece = true,
                caseTurn = Turn.Normal.Black,
                result = false,
            ),
            // 動かして（とって）防ごうとすると王手になるから詰み
            Param(
                caseCellPattern = listOf(
                    Pair(
                        Position(1, 9),
                        CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, Turn.Normal.Black)
                    ),
                    Pair(
                        Position(1, 8),
                        CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black)
                    ),
                    Pair(
                        Position(2, 9),
                        CellStatus.Fill.FromPiece(Piece.Surface.Keima, Turn.Normal.Black)
                    ),
                    Pair(
                        Position(2, 8),
                        CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black)
                    ),
                    Pair(
                        Position(2, 7),
                        CellStatus.Fill.FromPiece(Piece.Surface.Keima, Turn.Normal.White)
                    ),
                    Pair(
                        Position(9, 1),
                        CellStatus.Fill.FromPiece(Piece.Surface.Kaku, Turn.Normal.White)
                    ),
                ),
                caseUseStandPiece = false,
                caseTurn = Turn.Normal.Black,
                result = true,
            ),
            // 後手
            // 逃げ場なし & 取れる駒なし & 動かして防げる駒なし & 打って防げる状態なし
            Param(
                caseCellPattern = listOf(
                    Pair(
                        Position(5, 1),
                        CellStatus.Fill.FromPiece(Piece.Surface.Ou, Turn.Normal.White)
                    ),
                    Pair(
                        Position(5, 2),
                        CellStatus.Fill.FromPiece(Piece.Surface.Kin, Turn.Normal.Black)
                    ),
                    Pair(
                        Position(5, 3),
                        CellStatus.Fill.FromPiece(Piece.Surface.Kin, Turn.Normal.Black)
                    ),
                ),
                caseUseStandPiece = true,
                caseTurn = Turn.Normal.White,
                result = true,
            ),
            // 逃げ場あり & 取れる駒なし & 動かして防げる駒なし & 打って防げる状態なし
            Param(
                caseCellPattern = listOf(
                    Pair(
                        Position(5, 2),
                        CellStatus.Fill.FromPiece(Piece.Surface.Ou, Turn.Normal.White)
                    ),
                    Pair(
                        Position(5, 3),
                        CellStatus.Fill.FromPiece(Piece.Surface.Kin, Turn.Normal.Black)
                    ),
                    Pair(
                        Position(5, 4),
                        CellStatus.Fill.FromPiece(Piece.Surface.Kin, Turn.Normal.Black)
                    ),
                ),
                caseUseStandPiece = true,
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            // 逃げ場なし & 取れる駒あり & 動かして防げる駒なし & 打って防げる状態なし
            Param(
                caseCellPattern = listOf(
                    Pair(
                        Position(5, 1),
                        CellStatus.Fill.FromPiece(Piece.Surface.Ou, Turn.Normal.White)
                    ),
                    Pair(
                        Position(5, 2),
                        CellStatus.Fill.FromPiece(Piece.Surface.Kin, Turn.Normal.Black)
                    ),
                    Pair(
                        Position(5, 3),
                        CellStatus.Fill.FromPiece(Piece.Surface.Kin, Turn.Normal.Black)
                    ),
                    Pair(
                        Position(4, 2),
                        CellStatus.Fill.FromPiece(Piece.Surface.Hisya, Turn.Normal.White)
                    ),
                ),
                caseUseStandPiece = true,
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            // 逃げ場なし & 取れる駒なし & 動かして防げる駒あり & 打って防げる状態なし
            Param(
                caseCellPattern = listOf(
                    Pair(
                        Position(1, 1),
                        CellStatus.Fill.FromPiece(Piece.Surface.Ou, Turn.Normal.White)
                    ),
                    Pair(
                        Position(2, 1),
                        CellStatus.Fill.FromPiece(Piece.Surface.Keima, Turn.Normal.White)
                    ),
                    Pair(
                        Position(2, 2),
                        CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.White)
                    ),
                    Pair(
                        Position(1, 9),
                        CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, Turn.Normal.Black)
                    ),
                ),
                caseUseStandPiece = false,
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            // 逃げ場なし & 取れる駒なし & 動かして防げる駒なし & 打って防げる状態あり
            Param(
                caseCellPattern = listOf(
                    Pair(
                        Position(1, 1),
                        CellStatus.Fill.FromPiece(Piece.Surface.Ou, Turn.Normal.White)
                    ),
                    Pair(
                        Position(2, 1),
                        CellStatus.Fill.FromPiece(Piece.Surface.Keima, Turn.Normal.White)
                    ),
                    Pair(
                        Position(2, 2),
                        CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, Turn.Normal.White)
                    ),
                    Pair(
                        Position(1, 9),
                        CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, Turn.Normal.Black)
                    ),
                ),
                caseUseStandPiece = true,
                caseTurn = Turn.Normal.White,
                result = false,
            ),
            // 動かして（とって）防ごうとすると王手になるから詰み
            Param(
                caseCellPattern = listOf(
                    Pair(
                        Position(1, 1),
                        CellStatus.Fill.FromPiece(Piece.Surface.Ou, Turn.Normal.White)
                    ),
                    Pair(
                        Position(1, 2),
                        CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.White)
                    ),
                    Pair(
                        Position(2, 1),
                        CellStatus.Fill.FromPiece(Piece.Surface.Keima, Turn.Normal.White)
                    ),
                    Pair(
                        Position(2, 2),
                        CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.White)
                    ),
                    Pair(
                        Position(2, 3),
                        CellStatus.Fill.FromPiece(Piece.Surface.Keima, Turn.Normal.Black)
                    ),
                    Pair(
                        Position(9, 9),
                        CellStatus.Fill.FromPiece(Piece.Surface.Kaku, Turn.Normal.Black)
                    ),
                ),
                caseUseStandPiece = false,
                caseTurn = Turn.Normal.White,
                result = true,
            ),
        )

        // result
        params.forEach {
            val board = Board().apply {
                it.caseCellPattern.forEach { (position, cellStatus) ->
                    update(position, cellStatus)
                }
            }
            val stand = Stand().apply {
                if (!it.caseUseStandPiece) return@apply
                add(Piece.Surface.Kin)
            }
            val expected = gameService.isCheckmate(board, stand, it.caseTurn)
            assertEquals(expected, it.result)
        }
    }

    @Test
    fun `勝利判定`() {
        data class Param(
            val caseGameRule: GameRule,
            val caseIsCheck: Boolean = false,
            val caseIsCheckmate: Boolean = false,
            val caseNotKing: Boolean = false,
            val result: Boolean,
        )

        val ruleNormal = GameRule(
            boardRule = BoardRule(),
            usersRule = UsersRule(
                blackRule = UserRule(),
                whiteRule = UserRule(),
            )
        )
        val params = listOf(
            // 王手状態
            Param(
                caseGameRule = ruleNormal,
                caseIsCheck = true,
                result = true,
            ),
            // 王様がいない（とった）
            Param(
                caseGameRule = ruleNormal,
                caseNotKing = true,
                result = true,
            ),
            // 詰み
            Param(
                caseGameRule = ruleNormal,
                caseIsCheck = true,
                result = true,
            ),
            // 詰みでも王様がいないわけでもない
            Param(
                caseGameRule = ruleNormal,
                caseIsCheck = false,
                caseIsCheckmate = false,
                caseNotKing = false,
                result = false,
            ),
        )

        params.forEach {
            val board = Board().apply {
                val kingPosition = Position(5, 9)
                val king = CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, Turn.Normal.Black)
                update(kingPosition, king)
                when {
                    it.caseIsCheck -> {
                        val position1 = Position(5, 7)
                        val position2 = Position(5, 8)
                        val kin = CellStatus.Fill.FromPiece(Piece.Surface.Kin, Turn.Normal.White)
                        update(position1, kin)
                        update(position2, kin)
                    }

                    it.caseIsCheckmate -> {
                        val position = Position(5, 5)
                        val cell = CellStatus.Fill.FromPiece(Piece.Surface.Hisya, Turn.Normal.White)
                        update(position, cell)
                    }

                    it.caseNotKing -> {
                        update(kingPosition, CellStatus.Empty)
                    }
                }
            }
            val stand = Stand()
            val turn = Turn.Normal.White
            val expected = gameService.checkGameSet(board, stand, turn)
            assertEquals(expected, it.result)
        }
    }

    @Test
    fun `王手将棋件勝利判定`() {
        data class Param(
            val caseGameRule: GameRule,
            val caseIsCheck: Boolean = false,
            val caseIsCheckmate: Boolean = false,
            val caseNotKing: Boolean = false,
            val result: Boolean,
        )

        val ruleNormal = GameRule(
            boardRule = BoardRule(),
            usersRule = UsersRule(
                blackRule = UserRule(),
                whiteRule = UserRule(),
            )
        )
        val ruleIsFirstCheck =
            GameRule(
                boardRule = BoardRule(),
                usersRule = UsersRule(
                    blackRule = UserRule(
                        isFirstCheckEnd = true,
                    ),
                    whiteRule = UserRule(
                        isFirstCheckEnd = true,
                    ),
                )
            )
        val params = listOf(
            // 王手将棋設定ありで王手状態
            Param(
                caseGameRule = ruleIsFirstCheck,
                caseIsCheck = true,
                result = true,
            ),
            // 王手将棋設定ありで王手状態ではない
            Param(
                caseGameRule = ruleIsFirstCheck,
                caseIsCheck = false,
                result = false,
            ),
            // 王手将棋設定なしで王手状態
            Param(
                caseGameRule = ruleNormal,
                caseIsCheck = true,
                result = false,
            ),
        )

        params.forEach {
            val board = Board().apply {
                val kingPosition = Position(5, 9)
                val king = CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, Turn.Normal.Black)
                update(kingPosition, king)
                when {
                    it.caseIsCheck -> {
                        val position1 = Position(5, 7)
                        val position2 = Position(5, 8)
                        val kin = CellStatus.Fill.FromPiece(Piece.Surface.Kin, Turn.Normal.White)
                        update(position1, kin)
                        update(position2, kin)
                    }

                    it.caseIsCheckmate -> {
                        val position = Position(5, 5)
                        val cell = CellStatus.Fill.FromPiece(Piece.Surface.Hisya, Turn.Normal.White)
                        update(position, cell)
                    }

                    it.caseNotKing -> {
                        update(kingPosition, CellStatus.Empty)
                    }
                }
            }
            val turn = Turn.Normal.White
            val expected = gameService.checkGameSetForFirstCheck(board, turn, it.caseGameRule)
            assertEquals(expected, it.result)
        }
    }
}
