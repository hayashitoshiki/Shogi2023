package com.example.service

import com.example.entity.game.board.Cell
import com.example.entity.game.board.CellStatus
import com.example.entity.game.board.Position
import com.example.entity.game.board.Size
import com.example.entity.game.rule.PieceSetUpRule
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
}