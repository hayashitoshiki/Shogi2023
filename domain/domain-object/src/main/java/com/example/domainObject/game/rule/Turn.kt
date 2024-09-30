package com.example.domainObject.game.rule

/**
 * 手番
 *
 */
sealed interface Turn {

    /**
     * １体１（通常ルール）
     *
     */
    sealed interface Normal : Turn {

        /**
         * 先手
         *
         */
        data object Black : Normal

        /**
         * 後手
         *
         */
        data object White : Normal


        /**
         * 相手の手番取得
         *
         * @return 相手の手番
         */
        fun Turn.getOpponentTurn(): Turn {
            return when (this) {
                Black -> White
                White -> Black
            }
        }

        /**
         * 相手の手番取得
         *
         * @return 相手の手番
         */
        fun Turn.getBeforeTurn(): Turn {
            return when (this) {
                Black -> White
                White -> Black
            }
        }
    }

    companion object
}
