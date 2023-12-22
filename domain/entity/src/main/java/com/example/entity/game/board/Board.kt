package com.example.entity.game.board

import com.example.entity.game.rule.Turn

/**
 * 将棋盤
 *
 * @param size 将棋盤のマス目
 */
data class Board(val size: Size = Size(9, 9)) {
    private val board: List<List<Cell>> = List(size.row - 1) {
        List(size.column - 1) {
            Cell()
        }
    }

    /**
     * マス目を更新
     *
     * @param status マス目の情報
     * @param position マス目
     */
    fun update(position: Position, status: CellStatus) {
        board[position.row - 1][position.column - 1].update(status)
    }

    /**
     * マス目の情報取得
     *
     * @param position マス目
     * @return マス目の情報
     */
    fun getCellByPosition(position: Position): Cell = board[position.row - 1][position.column - 1]

    /**
     * 盤面クリア
     *
     */
    fun clear() {
        board.forEach { row ->
            row.forEach { cell ->
                cell.update(CellStatus.Empty)
            }
        }
    }

    /**
     * 空のマス目一覧取得
     *
     * @return　空のマス目一覧
     */
    fun getCellsFromEmpty(): List<Position> = getCellsBy(SearchParam.Empty)

    /**
     * 指定した手番の駒があるマス目一覧取得
     *
     * @return　空のマス目一覧
     */
    fun getCellsFromTurn(turn: Turn): List<Position> =
        getCellsBy(SearchParam.Piece.MatchTurn(turn))

    /**
     * 指定した手番の駒がないマス目一覧取得
     *
     * @return　空のマス目一覧
     */
    fun getCellsFromNotTurn(turn: Turn): List<Position> =
        getCellsBy(SearchParam.Piece.NotMatchTurn(turn))

    /**
     * 指定した条件に一致するマス一覧取得
     *
     * @param param 検索条件
     * @return 条件に一致したマス目一覧
     */
    private fun getCellsBy(param: SearchParam): List<Position> {
        return board.flatMapIndexed { row, list ->
            list.mapIndexedNotNull { column, cell ->
                when (val cellInfo = cell.getStatus()) {
                    CellStatus.Empty -> when (param) {
                        SearchParam.Empty,
                        is SearchParam.Piece.NotMatchTurn -> Position(row, column)

                        is SearchParam.Piece.MatchTurn -> null
                    }

                    is CellStatus.Fill.FromPiece -> {
                        when (param) {
                            SearchParam.Empty -> null
                            is SearchParam.Piece.NotMatchTurn -> {
                                val isTurnMatch = cellInfo.turn == param.turn
                                if (isTurnMatch) {
                                    null
                                } else {
                                    Position(row, column)
                                }
                            }

                            is SearchParam.Piece.MatchTurn -> {
                                val isTurnMatch = cellInfo.turn == param.turn
                                if (isTurnMatch) {
                                    Position(row, column)
                                } else {
                                    null
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * マス目の検索条件
     *
     */
    private sealed interface SearchParam {

        /**
         * 空であるマス
         */
        data object Empty : SearchParam

        /**
         * 駒あり
         *
         * @property turn 手番
         */
        sealed interface Piece : SearchParam {
            val turn: Turn

            /**
             * 指定した手番の駒があるマス
             */
            data class MatchTurn(override val turn: Turn) : Piece

            /**
             * 指定した手番の駒が含まれないマス
             */
            data class NotMatchTurn(override val turn: Turn) : Piece
        }
    }

    companion object
}
