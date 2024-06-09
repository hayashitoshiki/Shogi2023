package com.example.domainObject.game.piece

import com.example.domainObject.game.board.Position
import com.example.domainObject.game.rule.Turn

/**
 * 駒の動き
 *
 */
sealed interface Move {

    /**
     * １マス
     *
     */
    sealed interface One : Move {

        fun getPosition(turn: Turn): Position

        /**
         * 前
         */
        data object Front : One {

            override fun getPosition(turn: Turn): Position {
                return when (turn) {
                    Turn.Normal.Black -> Position(0, -1)
                    Turn.Normal.White -> Position(0, 1)
                }
            }
        }

        /**
         * 後ろ
         */
        data object Back : One {

            override fun getPosition(turn: Turn): Position {
                return when (turn) {
                    Turn.Normal.Black -> Position(0, 1)
                    Turn.Normal.White -> Position(0, -1)
                }
            }
        }

        /**
         * 右
         */
        data object Right : One {

            override fun getPosition(turn: Turn): Position {
                return when (turn) {
                    Turn.Normal.Black -> Position(-1, 0)
                    Turn.Normal.White -> Position(1, 0)
                }
            }
        }

        /**
         *左
         */
        data object Left : One {

            override fun getPosition(turn: Turn): Position {
                return when (turn) {
                    Turn.Normal.Black -> Position(1, 0)
                    Turn.Normal.White -> Position(-1, 0)
                }
            }
        }

        /**
         * 斜め右前
         */
        data object FrontRight : One {

            override fun getPosition(turn: Turn): Position {
                return when (turn) {
                    Turn.Normal.Black -> Position(-1, -1)
                    Turn.Normal.White -> Position(1, 1)
                }
            }
        }

        /**
         * 斜め左前
         */
        data object FrontLeft : One {

            override fun getPosition(turn: Turn): Position {
                return when (turn) {
                    Turn.Normal.Black -> Position(1, -1)
                    Turn.Normal.White -> Position(-1, 1)
                }
            }
        }

        /**
         * 斜め右後ろ
         */
        data object BackRight : One {

            override fun getPosition(turn: Turn): Position {
                return when (turn) {
                    Turn.Normal.Black -> Position(-1, 1)
                    Turn.Normal.White -> Position(1, -1)
                }
            }
        }

        /**
         * 斜め左後ろ
         */
        data object BackLeft : One {

            override fun getPosition(turn: Turn): Position {
                return when (turn) {
                    Turn.Normal.Black -> Position(1, 1)
                    Turn.Normal.White -> Position(-1, -1)
                }
            }
        }

        /**
         * 斜め右（桂馬）
         */
        data object FrontRightKei : One {

            override fun getPosition(turn: Turn): Position {
                return when (turn) {
                    Turn.Normal.Black -> Position(-1, -2)
                    Turn.Normal.White -> Position(1, 2)
                }
            }
        }

        /**
         * 斜め左（桂馬）
         */
        data object FrontLeftKei : One {

            override fun getPosition(turn: Turn): Position {
                return when (turn) {
                    Turn.Normal.Black -> Position(1, -2)
                    Turn.Normal.White -> Position(-1, 2)
                }
            }
        }
    }

    /**
     * １マス
     *
     */
    sealed interface Endless : Move {

        fun getBasePosition(turn: Turn): Position

        /**
         * 前
         */
        data object Front : Endless {

            override fun getBasePosition(turn: Turn): Position = One.Front.getPosition(turn)
        }

        /**
         * 後ろ
         */
        data object Back : Endless {

            override fun getBasePosition(turn: Turn): Position = One.Back.getPosition(turn)
        }

        /**
         * 右
         */
        data object Right : Endless {

            override fun getBasePosition(turn: Turn): Position = One.Right.getPosition(turn)
        }

        /**
         *左
         */
        data object Left : Endless {

            override fun getBasePosition(turn: Turn): Position = One.Left.getPosition(turn)
        }

        /**
         * 斜め右前
         */
        data object FrontRight : Endless {

            override fun getBasePosition(turn: Turn): Position = One.FrontRight.getPosition(turn)
        }

        /**
         * 斜め左前
         */
        data object FrontLeft : Endless {

            override fun getBasePosition(turn: Turn): Position = One.FrontLeft.getPosition(turn)
        }

        /**
         * 斜め右後ろ
         */
        data object BackRight : Endless {

            override fun getBasePosition(turn: Turn): Position = One.BackRight.getPosition(turn)
        }

        /**
         * 斜め左後ろ
         */
        data object BackLeft : Endless {

            override fun getBasePosition(turn: Turn): Position = One.BackLeft.getPosition(turn)
        }
    }
}