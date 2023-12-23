package com.example.extention

import com.example.entity.game.board.Board
import com.example.entity.game.board.CellStatus
import com.example.entity.game.board.Position
import com.example.entity.game.piece.Move
import com.example.entity.game.rule.PieceSetUpRule
import com.example.entity.game.rule.Turn

/**
 * 将棋盤のセットアップ
 *
 * @param pieceSetUpRule 設定内容
 * @return 設定内容に従った将棋盤の初期値
 */
internal fun Board.Companion.setUp(pieceSetUpRule: PieceSetUpRule): Board {
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
internal fun Board.searchMoveBy(position: Position, turn: Turn): List<Position> {
    val cellStatus = this.getCellByPosition(position).getStatus()
    if (cellStatus !is CellStatus.Fill.FromPiece) return emptyList()
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
                        val isFilledCell = this.getCellByPosition(previousPosition)
                            .getStatus() is CellStatus.Fill.FromPiece
                        val isNotStop = !(isNotBasePosition && isFilledCell)
                        it in this.size && checkOnMovePiece(it, turn) && isNotStop
                    }
                    .toList()
            }

            else -> emptyList()
        }
    }
}

private fun Board.checkOnMovePiece(position: Position, turn: Turn): Boolean {
    return when (val cellStatus = this.getCellByPosition(position).getStatus()) {
        CellStatus.Empty -> true
        is CellStatus.Fill.FromPiece -> cellStatus.turn != turn
    }
}
