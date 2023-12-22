package com.example.entity.game.piece

/**
 * 将棋の駒
 *
 */
sealed interface Piece {

    /**
     * 動ける場所
     *
     */
    val moves: Set<Move>

    /**
     * 表面
     *
     */
    sealed interface Surface : Piece {

        /**
         * 成り
         *
         * @return 裏面
         */
        fun evolution(): Reverse?

        /**
         * 歩
         */
        data object Fu : Surface {

            override val moves: Set<Move> = setOf(Move.Front.One)

            override fun evolution(): Reverse = Reverse.To
        }

        /**
         * 桂馬
         */
        data object Keima : Surface {

            override val moves: Set<Move> = setOf(
                Move.FrontRightKei,
                Move.FrontLeftKei,
            )

            override fun evolution(): Reverse = Reverse.Narikei
        }

        /**
         * 香車
         */
        data object Kyosya : Surface {

            override val moves: Set<Move> = setOf(Move.Front.Endless)

            override fun evolution(): Reverse = Reverse.Narikyo
        }

        /**
         * 銀
         */
        data object Gin : Surface {

            override val moves: Set<Move> = setOf(
                Move.Front.One,
                Move.FrontLeft.One,
                Move.FrontRight.One,
                Move.BackLeft.One,
                Move.BackRight.One,
            )

            override fun evolution(): Reverse = Reverse.Narigin
        }

        /**
         * 金
         */
        data object Kin : Surface {

            override val moves: Set<Move> = setOf(
                Move.Front.One,
                Move.Back.One,
                Move.Left.One,
                Move.Right.One,
                Move.FrontLeft.One,
                Move.FrontRight.One,
            )

            override fun evolution(): Reverse? = null
        }

        /**
         * 角
         */
        data object Kaku : Surface {

            override val moves: Set<Move> = setOf(
                Move.FrontLeft.Endless,
                Move.FrontRight.Endless,
                Move.BackLeft.Endless,
                Move.BackRight.Endless,
            )

            override fun evolution(): Reverse = Reverse.Uma
        }

        /**
         * 飛車
         */
        data object Hisya : Surface {

            override val moves: Set<Move> = setOf(
                Move.Front.Endless,
                Move.Back.Endless,
                Move.Left.Endless,
                Move.Right.Endless,
            )

            override fun evolution(): Reverse = Reverse.Ryu
        }

        /**
         * 王
         */
        data object Ou : Surface {

            override val moves: Set<Move> = setOf(
                Move.Front.One,
                Move.Back.One,
                Move.Left.One,
                Move.Right.One,
                Move.FrontLeft.One,
                Move.FrontRight.One,
                Move.BackLeft.One,
                Move.BackRight.One,
            )

            override fun evolution(): Reverse? = null
        }

        /**
         * 玉
         */
        data object Gyoku : Surface {

            override val moves: Set<Move> = setOf(
                Move.Front.One,
                Move.Back.One,
                Move.Left.One,
                Move.Right.One,
                Move.FrontLeft.One,
                Move.FrontRight.One,
                Move.BackLeft.One,
                Move.BackRight.One,
            )

            override fun evolution(): Reverse? = null
        }

    }

    /**
     * 裏面
     *
     */
    sealed interface Reverse : Piece {

        /**
         * 退化
         *
         * @return 表面
         */
        fun degeneracy(): Surface

        /**
         * と金
         */
        data object To : Reverse {

            override val moves: Set<Move> = setOf(
                Move.Front.One,
                Move.Back.One,
                Move.Left.One,
                Move.Right.One,
                Move.FrontLeft.One,
                Move.FrontRight.One,
            )

            override fun degeneracy(): Surface = Surface.Fu
        }

        /**
         * 成桂
         */
        data object Narikei : Reverse {

            override val moves: Set<Move> = setOf(
                Move.Front.One,
                Move.Back.One,
                Move.Left.One,
                Move.Right.One,
                Move.FrontLeft.One,
                Move.FrontRight.One,
            )

            override fun degeneracy(): Surface = Surface.Keima
        }

        /**
         * 成香
         */
        data object Narikyo : Reverse {

            override val moves: Set<Move> = setOf(
                Move.Front.One,
                Move.Back.One,
                Move.Left.One,
                Move.Right.One,
                Move.FrontLeft.One,
                Move.FrontRight.One,
            )

            override fun degeneracy(): Surface = Surface.Kyosya
        }

        /**
         * 成銀
         */
        data object Narigin : Reverse {

            override val moves: Set<Move> = setOf(
                Move.Front.One,
                Move.Back.One,
                Move.Left.One,
                Move.Right.One,
                Move.FrontLeft.One,
                Move.FrontRight.One,
            )

            override fun degeneracy(): Surface = Surface.Gin
        }

        /**
         * 馬
         */
        data object Uma : Reverse {

            override val moves: Set<Move> = setOf(
                Move.Front.One,
                Move.Back.One,
                Move.Left.One,
                Move.Right.One,
                Move.FrontLeft.Endless,
                Move.FrontRight.Endless,
                Move.BackLeft.Endless,
                Move.BackRight.Endless,
            )

            override fun degeneracy(): Surface = Surface.Kaku
        }

        /**
         * 竜
         */
        data object Ryu : Reverse {

            override val moves: Set<Move> = setOf(
                Move.Front.Endless,
                Move.Back.Endless,
                Move.Left.Endless,
                Move.Right.Endless,
                Move.FrontLeft.One,
                Move.FrontRight.One,
                Move.BackLeft.One,
                Move.BackRight.One,
            )

            override fun degeneracy(): Surface = Surface.Hisya
        }
    }
}
