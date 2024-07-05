package com.example.domainLogic.board

import com.example.domainLogic.piece.evolution
import com.example.domainLogic.piece.isAvailablePut
import com.example.domainLogic.piece.shouldEvolution
import com.example.domainLogic.rule.getOpponentTurn
import com.example.domainObject.game.board.Board
import com.example.domainObject.game.board.CellStatus
import com.example.domainObject.game.board.EvolutionCheckState
import com.example.domainObject.game.board.Position
import com.example.domainObject.game.piece.Move
import com.example.domainObject.game.piece.Piece
import com.example.domainObject.game.rule.BoardRule
import com.example.domainObject.game.rule.Hande
import com.example.domainObject.game.rule.Turn

/**
 * 将棋盤のセットアップ
 *
 * @param gameRule 設定内容
 * @return 設定内容に従った将棋盤の初期値
 */
fun Board.Companion.setUp(boardRule: BoardRule): Board {
    return Board(boardRule.boardSize).apply {
        val initPiece = boardRule.setUpRule.toSetUpPiece()
            .setHandBlack(boardRule.boardHandeRule.blackHande)
            .setHandeWhite(boardRule.boardHandeRule.whiteHande)
        initPiece.forEach { (position, cellStatus) -> update(position, cellStatus) }
    }
}

/**
 * 盤面駒配置
 *
 * @return 初期設定値で駒を配置した盤面
 */
private fun BoardRule.SetUpRule.toSetUpPiece(): Map<Position, CellStatus> {
    return when (this) {
        BoardRule.SetUpRule.NORMAL -> SetUpPiece.Normal.normalInitPiece
    }
}

/**
 * 先手のハンデ設定
 *
 * @return 先手のハンデを設定した盤面
 */
private fun Map<Position, CellStatus>.setHandBlack(hande: Hande): Map<Position, CellStatus> {
    return when (hande) {
        Hande.NON -> this
        Hande.KAKU -> this.minus(SetUpPiece.Normal.blackKaku.first)
        Hande.HISYA -> this.minus(SetUpPiece.Normal.blackHisya.first)
        Hande.TWO -> this.minus(SetUpPiece.Normal.blackKaku.first)
            .minus(SetUpPiece.Normal.blackHisya.first)

        Hande.FOR -> {
            this.minus(SetUpPiece.Normal.blackHisya.first)
                .minus(SetUpPiece.Normal.blackKaku.first)
                .minus(SetUpPiece.Normal.blackKyo.entries.map { it.key }.toSet())
        }

        Hande.SIX -> {
            this.minus(SetUpPiece.Normal.blackHisya.first)
                .minus(SetUpPiece.Normal.blackKaku.first)
                .minus(SetUpPiece.Normal.blackKei.entries.map { it.key }.toSet())
                .minus(SetUpPiece.Normal.blackKyo.entries.map { it.key }.toSet())
        }

        Hande.EIGHT -> {
            this.minus(SetUpPiece.Normal.blackHisya.first)
                .minus(SetUpPiece.Normal.blackKaku.first)
                .minus(SetUpPiece.Normal.blackKei.entries.map { it.key }.toSet())
                .minus(SetUpPiece.Normal.blackKyo.entries.map { it.key }.toSet())
                .minus(SetUpPiece.Normal.blackGin.entries.map { it.key }.toSet())
        }
    }
}

/**
 * 後手のハンデ設定
 *
 * @return 先手のハンデを設定した盤面
 */
private fun Map<Position, CellStatus>.setHandeWhite(hande: Hande): Map<Position, CellStatus> {
    return when (hande) {
        Hande.NON -> this
        Hande.KAKU -> this.minus(SetUpPiece.Normal.whiteKaku.first)
        Hande.HISYA -> this.minus(SetUpPiece.Normal.whiteHisya.first)
        Hande.TWO -> this.minus(SetUpPiece.Normal.whiteKaku.first)
            .minus(SetUpPiece.Normal.whiteHisya.first)

        Hande.FOR -> {
            this.minus(SetUpPiece.Normal.whiteKaku.first)
                .minus(SetUpPiece.Normal.whiteHisya.first)
                .minus(SetUpPiece.Normal.whiteKyo.entries.map { it.key }.toSet())
        }

        Hande.SIX -> {
            this.minus(SetUpPiece.Normal.whiteKaku.first)
                .minus(SetUpPiece.Normal.whiteHisya.first)
                .minus(SetUpPiece.Normal.whiteKyo.entries.map { it.key }.toSet())
                .minus(SetUpPiece.Normal.whiteKei.entries.map { it.key }.toSet())
        }

        Hande.EIGHT -> {
            this.minus(SetUpPiece.Normal.whiteKaku.first)
                .minus(SetUpPiece.Normal.whiteHisya.first)
                .minus(SetUpPiece.Normal.whiteKyo.entries.map { it.key }.toSet())
                .minus(SetUpPiece.Normal.whiteKei.entries.map { it.key }.toSet())
                .minus(SetUpPiece.Normal.whiteGin.entries.map { it.key }.toSet())
        }
    }
}

/**
 * 駒を動かす
 *
 * @param beforePosition 動かしたい駒のマス目
 * @param afterPosition 動かす先のマス目
 * @return　動かした後の将棋盤
 */
fun Board.movePieceByPosition(beforePosition: Position, afterPosition: Position): Board {
    val beforePositionCell = this.getCellByPosition(beforePosition)
    this.update(afterPosition, beforePositionCell.getStatus())
    this.update(beforePosition, CellStatus.Empty)
    return this
}

/**
 * 指定したマスの駒が動かせるマスを返却
 *
 * @param position 動かしたい駒のマス目
 * @param turn 現在の手番
 * @return　動かせるマスのリスト
 */
fun Board.searchMoveBy(position: Position, turn: Turn): List<Position> {
    val cellStatus = this.getPieceOrNullByPosition(position) ?: return emptyList()
    if (cellStatus.turn != turn) return emptyList()

    return cellStatus.piece.moves.flatMap { move ->
        when (move) {
            is Move.One -> {
                val movePosition = move.getPosition(turn)
                val newPosition = position.add(movePosition)
                if (checkOnMovePiece(newPosition, turn)) {
                    listOf(newPosition)
                } else {
                    emptyList()
                }
            }

            is Move.Endless -> {
                val movePosition = move.getBasePosition(turn)
                generateSequence(position.add(movePosition)) { it.add(movePosition) }
                    .takeWhile {
                        val previousPosition = it.minus(movePosition)
                        val isNotBasePosition = previousPosition != position
                        val isFilledCell =
                            this.getCellByPosition(previousPosition).getStatus() is CellStatus.Fill
                        val isNotStop = !(isNotBasePosition && isFilledCell)
                        it in this.size && checkOnMovePiece(it, turn) && isNotStop
                    }
                    .toList()
            }

            else -> emptyList()
        }
    }
}

/**
 * 駒を持ち駒から打てる場所を探す
 *
 * @param piece 打つ駒
 * @param turn 手番
 * @return 打てる場所一覧
 */
fun Board.searchPutBy(piece: Piece, turn: Turn): List<Position> {
    return this.getCellsFromEmpty().filter { piece.isAvailablePut(this, it, turn) }
}

private fun Board.checkOnMovePiece(position: Position, turn: Turn): Boolean {
    if (position !in this.size) return false
    return this.getPieceOrNullByPosition(position)?.let { cellStatus ->
        cellStatus.turn != turn
    } ?: true
}

private fun Board.isKingCellBy(position: Position, turn: Turn): Boolean {
    val cellStatus = getPieceOrNullByPosition(position) ?: return false

    return when (turn) {
        Turn.Normal.Black -> cellStatus.piece == Piece.Surface.Gyoku
        Turn.Normal.White -> cellStatus.piece == Piece.Surface.Ou
    }
}

/**
 * 指定した手番の王様がいるか判定
 *
 * @param turn 手番
 * @return 王様がいるか
 */
fun Board.isAvailableKingBy(turn: Turn): Boolean {
    return this.getAllCells().any {
        val cell = it.value.getStatus() as? CellStatus.Fill.FromPiece ?: return@any false
        if (cell.turn != turn) return@any false
        cell.piece is Piece.Surface.Ou || cell.piece is Piece.Surface.Gyoku
    }
}

/**
 * 駒が成れるか判別
 *
 * @param beforePosition 動かす前のマス
 * @param afterPosition 動かした後のマス
 * @return 判定結果
 */
fun Board.checkPieceEvolution(
    piece: Piece.Surface,
    beforePosition: Position,
    afterPosition: Position,
    turn: Turn,
): EvolutionCheckState {
    return when {
        piece.shouldEvolution(this, afterPosition, turn) -> EvolutionCheckState.Should
        checkAvailablePieceEvolution(beforePosition, afterPosition) -> EvolutionCheckState.Choose
        else -> EvolutionCheckState.No
    }
}

/**
 * 駒が成れるか判別
 *
 * @param beforePosition 動かす前のマス
 * @param afterPosition 動かした後のマス
 * @return 判定結果
 */
private fun Board.checkAvailablePieceEvolution(
    beforePosition: Position,
    afterPosition: Position,
): Boolean {
    val cellStatus = this.getPieceOrNullByPosition(beforePosition) ?: return false
    val piece = cellStatus.piece as? Piece.Surface ?: return false
    if (piece.evolution() == null) return false
    val blackEvolutionArea = 3
    val whiteEvolutionArea = this.size.column - 3
    val beforeColumn = beforePosition.column
    val afterColumn = afterPosition.column

    return when (cellStatus.turn) {
        Turn.Normal.Black -> beforeColumn <= blackEvolutionArea || afterColumn <= blackEvolutionArea
        Turn.Normal.White -> beforeColumn > whiteEvolutionArea || afterColumn > whiteEvolutionArea
    }
}

/**
 * 指定した手番が王手されているか判定
 *
 * @param turn 王手されているか判定する手番
 * @return 王手しているかの判定結果
 */
fun Board.isCheckByTurn(turn: Turn): Boolean {
    val opponentTurn = turn.getOpponentTurn()
    return this.getCellsFromTurn(opponentTurn).any { position ->
        this.searchMoveBy(position, opponentTurn).any { movePosition ->
            this.isKingCellBy(movePosition, turn)
        }
    }
}

/**
 * 指定したマスの駒を成らせる
 *
 * @param position マス目
 * @return 適用した将棋盤
 */
fun Board.updatePieceEvolution(position: Position): Board {
    getPieceOrNullByPosition(position)?.let { cellStatus ->
        (cellStatus.piece as? Piece.Surface)?.evolution()?.let { evolvedPiece ->
            update(position, cellStatus.copy(piece = evolvedPiece))
        }
    }
    return this
}

object SetUpPiece {
    object Normal {
        val blackHisya = Pair(
            Position(2, 8),
            CellStatus.Fill.FromPiece(Piece.Surface.Hisya, Turn.Normal.Black),
        )
        val blackKaku = Pair(
            Position(8, 8),
            CellStatus.Fill.FromPiece(Piece.Surface.Kaku, Turn.Normal.Black),
        )
        val blackKyo = mapOf(
            Pair(
                Position(1, 9),
                CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, Turn.Normal.Black),
            ),
            Pair(
                Position(9, 9),
                CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, Turn.Normal.Black),
            ),
        )
        val blackKei = mapOf(
            Pair(
                Position(2, 9),
                CellStatus.Fill.FromPiece(Piece.Surface.Keima, Turn.Normal.Black),
            ),
            Pair(
                Position(8, 9),
                CellStatus.Fill.FromPiece(Piece.Surface.Keima, Turn.Normal.Black),
            ),
        )
        val blackGin = mapOf(
            Pair(
                Position(3, 9),
                CellStatus.Fill.FromPiece(Piece.Surface.Gin, Turn.Normal.Black),
            ),
            Pair(
                Position(7, 9),
                CellStatus.Fill.FromPiece(Piece.Surface.Gin, Turn.Normal.Black),
            ),
        )
        val blackKin = setOf(
            Pair(
                Position(4, 9),
                CellStatus.Fill.FromPiece(Piece.Surface.Kin, Turn.Normal.Black),
            ),
            Pair(
                Position(6, 9),
                CellStatus.Fill.FromPiece(Piece.Surface.Kin, Turn.Normal.Black),
            ),
        )
        val whiteHisya = Pair(
            Position(8, 2),
            CellStatus.Fill.FromPiece(Piece.Surface.Hisya, Turn.Normal.White),
        )
        val whiteKaku = Pair(
            Position(2, 2),
            CellStatus.Fill.FromPiece(Piece.Surface.Kaku, Turn.Normal.White),
        )
        val whiteKyo = mapOf(
            Pair(
                Position(1, 1),
                CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, Turn.Normal.White),
            ),
            Pair(
                Position(9, 1),
                CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, Turn.Normal.White),
            ),
        )
        val whiteKei = mapOf(
            Pair(
                Position(2, 1),
                CellStatus.Fill.FromPiece(Piece.Surface.Keima, Turn.Normal.White),
            ),
            Pair(
                Position(8, 1),
                CellStatus.Fill.FromPiece(Piece.Surface.Keima, Turn.Normal.White),
            ),
        )
        val whiteGin = mapOf(
            Pair(
                Position(3, 1),
                CellStatus.Fill.FromPiece(Piece.Surface.Gin, Turn.Normal.White),
            ),
            Pair(
                Position(7, 1),
                CellStatus.Fill.FromPiece(Piece.Surface.Gin, Turn.Normal.White),
            ),
        )
        val whiteKin = setOf(
            Pair(
                Position(4, 1),
                CellStatus.Fill.FromPiece(Piece.Surface.Kin, Turn.Normal.White),
            ),
            Pair(
                Position(6, 1),
                CellStatus.Fill.FromPiece(Piece.Surface.Kin, Turn.Normal.White),
            ),
        )
        val normalInitPiece: Map<Position, CellStatus> = mutableMapOf(
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
            .plus(whiteKyo)
            .plus(whiteKei)
            .plus(whiteGin)
            .plus(whiteKin)
            .plus(whiteHisya)
            .plus(whiteKaku)
            .plus(blackKyo)
            .plus(blackKei)
            .plus(blackGin)
            .plus(blackKin)
            .plus(blackHisya)
            .plus(blackKaku)
    }
}
