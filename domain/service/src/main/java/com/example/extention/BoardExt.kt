package com.example.extention

import com.example.entity.game.board.Board
import com.example.entity.game.board.CellStatus
import com.example.entity.game.board.EvolutionCheckState
import com.example.entity.game.board.Position
import com.example.entity.game.piece.Move
import com.example.entity.game.piece.Piece
import com.example.entity.game.rule.PieceSetUpRule
import com.example.entity.game.rule.Turn

/**
 * 将棋盤のセットアップ
 *
 * @param pieceSetUpRule 設定内容
 * @return 設定内容に従った将棋盤の初期値
 */
fun Board.Companion.setUp(pieceSetUpRule: PieceSetUpRule): Board {
    return Board(pieceSetUpRule.boardSize).apply {
        pieceSetUpRule.initPiece.forEach { (position, cellStatus) ->
            update(position, cellStatus)
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
internal fun Board.movePieceByPosition(beforePosition: Position, afterPosition: Position): Board {
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
