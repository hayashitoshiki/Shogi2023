package com.example.entity.game.piece

/**
 * 駒の動き
 *
 */
sealed interface Move {

    /**
     * 前
     */
    sealed interface Front : Move {

        /**
         * １マス
         */
        data object One : Front

        /**
         * 制限なし
         */
        data object Endless : Front
    }

    /**
     * 後ろ
     *
     */
    sealed interface Back : Move {

        /**
         * １マス
         */
        data object One : Back

        /**
         * 制限なし
         */
        data object Endless : Back
    }

    /**
     * 右
     */
    sealed interface Right : Move {

        /**
         * １マス
         */
        data object One : Back

        /**
         * 制限なし
         */
        data object Endless : Back
    }

    /**
     * 左
     */
    sealed interface Left : Move {

        /**
         * １マス
         */
        data object One : Back

        /**
         * 制限なし
         */
        data object Endless : Back
    }

    /**
     * 斜め右前
     */
    sealed interface FrontRight : Move {

        /**
         * １マス
         */
        data object One : Back

        /**
         * 制限なし
         */
        data object Endless : Back
    }

    /**
     * 斜め左前
     */
    sealed interface FrontLeft : Move {

        /**
         * １マス
         */
        data object One : Back

        /**
         * 制限なし
         */
        data object Endless : Back
    }

    /**
     * 斜め右後ろ
     */
    sealed interface BackRight : Move {

        /**
         * １マス
         */
        data object One : Back

        /**
         * 制限なし
         */
        data object Endless : Back
    }

    /**
     * 斜め左後ろ
     */
    sealed interface BackLeft : Move {

        /**
         * １マス
         */
        data object One : Back

        /**
         * 制限なし
         */
        data object Endless : Back
    }

    /**
     * 斜め右（桂馬）
     */
    data object FrontRightKei : Move

    /**
     * 斜め左（桂馬）
     */
    data object FrontLeftKei : Move
}