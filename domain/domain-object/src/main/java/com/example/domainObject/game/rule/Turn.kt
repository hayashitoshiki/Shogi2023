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
    }

    companion object
}
