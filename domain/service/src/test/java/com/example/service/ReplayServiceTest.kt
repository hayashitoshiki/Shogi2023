package com.example.service

import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.CellStatus
import com.example.domainObject.game.board.Position
import com.example.domainObject.game.board.Stand
import com.example.domainObject.game.log.MoveRecode
import com.example.domainObject.game.log.MoveTarget
import com.example.domainObject.game.piece.Piece
import com.example.domainObject.game.rule.Turn
import com.example.testDomainObject.board.fake
import com.example.testDomainObject.log.fake
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ReplayServiceTest {

  private val replayService = ReplayServiceImpl()

  private val black = Turn.Normal.Black
  private val white = Turn.Normal.White
  private fun initBoard() = Board.fake(
    cellList = listOf(
      Position(5, 1) to CellStatus.Fill.FromPiece(Piece.Surface.Ou, white),
      Position(1, 2) to CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, white),
      Position(9, 2) to CellStatus.Fill.FromPiece(Piece.Surface.Hisya, white),
      Position(5, 9) to CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, black),
      Position(1, 8) to CellStatus.Fill.FromPiece(Piece.Surface.Hisya, black),
      Position(9, 8) to CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, black),
    )
  )
  private fun createBlackStand() = Stand.fake(ginCount = 1)
  private fun createWhiteStand() = Stand.fake(ginCount = 1)

  private val case1AfterPosition = Position.fake(row = 1, column = 1)
  private val case2BeforePosition = Position.fake(row = 9, column = 8)
  private val case2AfterPosition = Position.fake(row = 9, column = 3)
  private val case4BeforePosition = Position.fake(row = 9, column = 8)
  private val case4AfterPosition = Position.fake(row = 9, column = 2)
  private val case6AfterPosition = Position.fake(row = 1, column = 7)
  private val case7BeforePosition = Position.fake(row = 1, column = 2)
  private val case7AfterPosition = Position.fake(row = 1, column = 7)
  private val case9BeforePosition = Position.fake(row = 1, column = 2)
  private val case9AfterPosition = Position.fake(row = 1, column = 8)
  private val case1Log = MoveRecode.fake(
    turn = black,
    moveTarget = MoveTarget.Stand.fake(
      piece = Piece.Surface.Gin,
    ),
    afterPosition = case1AfterPosition,
    isEvolution = false,
    takePiece = null,
  )
  private val case2Log = MoveRecode.fake(
    turn = black,
    moveTarget = MoveTarget.Board.fake(
      position = case2BeforePosition,
    ),
    afterPosition = case2AfterPosition,
    isEvolution = false,
    takePiece = null,
  )
  private val case4Log = MoveRecode.fake(
    turn = black,
    moveTarget = MoveTarget.Board.fake(
      position = case4BeforePosition,
    ),
    afterPosition = case4AfterPosition,
    isEvolution = false,
    takePiece = Piece.Surface.Hisya,
  )
  private val case6Log = MoveRecode.fake(
    turn = white,
    moveTarget = MoveTarget.Stand.fake(
      piece = Piece.Surface.Gin,
    ),
    afterPosition = case6AfterPosition,
    isEvolution = false,
    takePiece = null,
  )
  private val case7Log = MoveRecode.fake(
    turn = white,
    moveTarget = MoveTarget.Board.fake(
      position = case7BeforePosition,
    ),
    afterPosition = case7AfterPosition,
    isEvolution = false,
    takePiece = null,
  )
  private val case9Log = MoveRecode.fake(
    turn = white,
    moveTarget = MoveTarget.Board.fake(
      position = case9BeforePosition,
    ),
    afterPosition = case9AfterPosition,
    isEvolution = false,
    takePiece = Piece.Surface.Hisya,
  )

  private val params = listOf(
    Param(
      case = "先手が持ち駒を打って進める",
      log = case1Log,
      expectedBoard = initBoard().let {
        it.update(case1AfterPosition, CellStatus.Fill.FromPiece(Piece.Surface.Gin, black))
        it
      },
      expectedBlackStand = createBlackStand().let {
        it.remove(Piece.Surface.Gin)
        it
      },
      expectedWhiteStand = createWhiteStand(),
    ),
    Param(
      case = "先手が盤上の駒を使って成らずに駒も取らずに進める",
      log = case2Log,
      expectedBoard = initBoard().let {
        it.update(case2BeforePosition, CellStatus.Empty)
        it.update(case2AfterPosition, CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, black))
        it
      },
      expectedBlackStand = createBlackStand(),
      expectedWhiteStand = createWhiteStand(),
    ),
    Param(
      case = "先手が盤上の駒を使って成って進める",
      log = case2Log.copy(isEvolution = true),
      expectedBoard = initBoard().let {
        it.update(case2BeforePosition, CellStatus.Empty)
        it.update(case2AfterPosition, CellStatus.Fill.FromPiece(Piece.Reverse.Narikyo, black))
        it
      },
      expectedBlackStand = createBlackStand(),
      expectedWhiteStand = createWhiteStand(),
    ),
    Param(
      case = "先手が盤上の駒を使って駒をとって進める",
      log = case4Log,
      expectedBoard = initBoard().let {
        it.update(case4BeforePosition, CellStatus.Empty)
        it.update(case4AfterPosition, CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, black))
        it
      },
      expectedBlackStand = createBlackStand().let {
        it.add(Piece.Surface.Hisya)
        it
      },
      expectedWhiteStand = createWhiteStand(),
    ),
    Param(
      "先手が盤上の駒を使って成りつつ駒をとって進める",
      log = case4Log.copy(isEvolution = true),
      expectedBoard = initBoard().let {
        it.update(case4BeforePosition, CellStatus.Empty)
        it.update(case4AfterPosition, CellStatus.Fill.FromPiece(Piece.Reverse.Narikyo, black))
        it
      },
      expectedBlackStand = createBlackStand().let {
        it.add(Piece.Surface.Hisya)
        it
      },
      expectedWhiteStand = createWhiteStand(),
    ),
    Param(
      case = "後手が持ち駒を打って進める",
      log = case6Log,
      expectedBoard = initBoard().let {
        it.update(case6AfterPosition, CellStatus.Fill.FromPiece(Piece.Surface.Gin, white))
        it
      },
      expectedBlackStand = createBlackStand(),
      expectedWhiteStand = createWhiteStand().let {
        it.remove(Piece.Surface.Gin)
        it
      },
    ),
    Param(
      case = "後手が盤上の駒を使って成らずに駒も取らずに進める",
      log = case7Log,
      expectedBoard  = initBoard().let {
        it.update(case7BeforePosition, CellStatus.Empty)
        it.update(case7AfterPosition, CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, white))
        it
      },
      expectedBlackStand = createBlackStand(),
      expectedWhiteStand = createWhiteStand(),
    ),
    Param(
      case = "後手が盤上の駒を使って成って進める",
      log = case7Log.copy(isEvolution = true),
      expectedBoard = initBoard().let {
        it.update(case7BeforePosition, CellStatus.Empty)
        it.update(case7AfterPosition, CellStatus.Fill.FromPiece(Piece.Reverse.Narikyo, white))
        it
      },
      expectedBlackStand = createBlackStand(),
      expectedWhiteStand = createWhiteStand(),
    ),
    Param(
      case = "後手が盤上の駒を使って駒をとって進める",
      log = case9Log,
      expectedBoard = initBoard().let {
        it.update(case9BeforePosition, CellStatus.Empty)
        it.update(case9AfterPosition, CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, white))
        it
      },
      expectedBlackStand = createBlackStand(),
      expectedWhiteStand = createWhiteStand().let {
        it.add(Piece.Surface.Hisya)
        it
      },
    ),
    Param(
      "後手が盤上の駒を使って成りつつ駒をとって進める",
      log = case9Log.copy(isEvolution = true),
      expectedBoard = initBoard().let {
        it.update(case9BeforePosition, CellStatus.Empty)
        it.update(case9AfterPosition, CellStatus.Fill.FromPiece(Piece.Reverse.Narikyo, white))
        it
      },
      expectedBlackStand = createBlackStand(),
      expectedWhiteStand = createWhiteStand().let {
        it.add(Piece.Surface.Hisya)
        it
      },
    ),
  )

  @Test
  fun `１手進める`() {
    params.forEach { param ->
      val result = replayService.goNext(initBoard(), createBlackStand(), createWhiteStand(), param.log)
      assertEquals(param.case, param.expectedBoard, result.first)
      assertEquals(param.case, param.expectedBlackStand, result.second)
      assertEquals(param.case, param.expectedWhiteStand, result.third)
    }
  }

  @Test
  fun `１手戻す`() {
    params.forEach { param ->
      val result = replayService.goBack(param.expectedBoard, param.expectedBlackStand, param.expectedWhiteStand, param.log)
      assertEquals(param.case, initBoard(), result.first)
      assertEquals(param.case, createBlackStand(), result.second)
      assertEquals(param.case, createWhiteStand(), result.third)
    }
  }

  data class Param(
    val case: String,
    val log: MoveRecode,
    val expectedBoard: Board,
    val expectedBlackStand: Stand,
    val expectedWhiteStand: Stand,
  )
}