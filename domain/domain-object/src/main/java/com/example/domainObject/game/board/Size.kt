package com.example.domainObject.game.board

/**
 * 将棋盤のサイズ
 *
 * 縦横両方合わせて２マス以上であること
 * @property row 横（1以上であること）
 * @property column 縦（1以上であること）
 */
data class Size(val row: Int, val column: Int) {

    init {
        require(isValid(row, column))
    }

    companion object {

        fun isValid(row: Int, column: Int): Boolean {
            return when {
                row <= 0 -> false
                column <= 1 -> false
                else -> true
            }
        }

        fun getOrNull(row: Int, column: Int): Size? =
            if (isValid(row, column)) Size(row, column) else null
    }
}
