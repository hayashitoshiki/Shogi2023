package com.example.entity.game.board

import com.example.entity.game.rule.Turn

/**
 * 将棋盤
 *
 * @param size 将棋盤のマス目
 */
data class Board(val size: Size = Size(9, 9)) {

    private val board: List<List<Cell>> = List(size.row) {
        List(size.column) {
            Cell()
        }
    }
    private val blackPiecePositionList: MutableList<Position> = mutableListOf()
    private val whitePiecePositionList: MutableList<Position> = mutableListOf()
    private val pieceEmptyPositionList: MutableList<Position> = (1..size.row).flatMap { row ->
        (1..size.column).map { column ->
            Position(row, column)
        }
    }.toMutableList()

    /**
     * マス目を更新
     *
     * @param status マス目の情報
     * @param position マス目
     */
    fun update(position: Position, status: CellStatus) {
        val row = position.row - 1
        val column = position.column - 1
        val beforeCell = board[row][column].getStatus()
        board[row][column].update(status)

        when (beforeCell) {
            CellStatus.Empty -> pieceEmptyPositionList.remove(position)
            is CellStatus.Fill.FromPiece -> when (beforeCell.turn) {
                Turn.Normal.Black -> blackPiecePositionList.remove(position)
                Turn.Normal.White -> whitePiecePositionList.remove(position)
            }
        }
        when (status) {
            CellStatus.Empty -> pieceEmptyPositionList.add(position)
            is CellStatus.Fill.FromPiece -> when (status.turn) {
                Turn.Normal.Black -> blackPiecePositionList.add(position)
                Turn.Normal.White -> whitePiecePositionList.add(position)
            }
        }
    }

    /**
     * マス目の情報取得
     *
     * @param position マス目
     * @return マス目の情報
     */
    fun getCellByPosition(position: Position): Cell = board[position.row - 1][position.column - 1]

    fun getAllCells(): Map<Position, Cell> {
        return board.flatMapIndexed { row, cells ->
            cells.mapIndexed { column, cell ->
                Position(row + 1, column + 1) to cell
            }
        }.toMap()
    }

    /**
     * 盤面クリア
     *
     */
    fun clear() {
        (1..size.row).flatMap { row ->
            (1..size.column).map { column ->
                update(Position(row, column), CellStatus.Empty)
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
        return when (param) {
            SearchParam.Empty -> pieceEmptyPositionList
            is SearchParam.Piece.MatchTurn -> when (param.turn) {
                Turn.Normal.Black -> blackPiecePositionList
                Turn.Normal.White -> whitePiecePositionList
            }

            is SearchParam.Piece.NotMatchTurn -> when (param.turn) {
                Turn.Normal.Black -> pieceEmptyPositionList + whitePiecePositionList
                Turn.Normal.White -> pieceEmptyPositionList + blackPiecePositionList
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

    /**
     * Boardのcopy()
     *
     * listが値わたしできないため拡張
     */
    fun copy(): Board {
        val copiedBoard = Board(size)
        // リストの内容を手動でコピーする
        this.getAllCells().forEach { (position, cell) ->
            copiedBoard.update(position, cell.getStatus())
        }
        return copiedBoard
    }

    companion object
}
