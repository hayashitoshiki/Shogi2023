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

            override val moves: Set<Move> = setOf(Move.One.Front)

            override fun evolution(): Reverse = Reverse.To
        }

        /**
         * 桂馬
         */
        data object Keima : Surface {

            override val moves: Set<Move> = setOf(
                Move.One.FrontRightKei,
                Move.One.FrontLeftKei,
            )

            override fun evolution(): Reverse = Reverse.Narikei
        }

        /**
         * 香車
         */
        data object Kyosya : Surface {

            override val moves: Set<Move> = setOf(Move.Endless.Front)

            override fun evolution(): Reverse = Reverse.Narikyo
        }

        /**
         * 銀
         */
        data object Gin : Surface {

            override val moves: Set<Move> = setOf(
                Move.One.Front,
                Move.One.FrontLeft,
                Move.One.FrontRight,
                Move.One.BackLeft,
                Move.One.BackRight,
            )

            override fun evolution(): Reverse = Reverse.Narigin
        }

        /**
         * 金
         */
        data object Kin : Surface {

            override val moves: Set<Move> = setOf(
                Move.One.Front,
                Move.One.Back,
                Move.One.Left,
                Move.One.Right,
                Move.One.FrontLeft,
                Move.One.FrontRight,
            )

            override fun evolution(): Reverse? = null
        }

        /**
         * 角
         */
        data object Kaku : Surface {

            override val moves: Set<Move> = setOf(
                Move.Endless.FrontLeft,
                Move.Endless.FrontRight,
                Move.Endless.BackLeft,
                Move.Endless.BackRight,
            )

            override fun evolution(): Reverse = Reverse.Uma
        }

        /**
         * 飛車
         */
        data object Hisya : Surface {

            override val moves: Set<Move> = setOf(
                Move.Endless.Front,
                Move.Endless.Back,
                Move.Endless.Left,
                Move.Endless.Right,
            )

            override fun evolution(): Reverse = Reverse.Ryu
        }

        /**
         * 王
         */
        data object Ou : Surface {

            override val moves: Set<Move> = setOf(
                Move.One.Front,
                Move.One.Back,
                Move.One.Left,
                Move.One.Right,
                Move.One.FrontLeft,
                Move.One.FrontRight,
                Move.One.BackLeft,
                Move.One.BackRight,
            )

            override fun evolution(): Reverse? = null
        }

        /**
         * 玉
         */
        data object Gyoku : Surface {

            override val moves: Set<Move> = Ou.moves

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

            override val moves: Set<Move> = Surface.Kin.moves

            override fun degeneracy(): Surface = Surface.Fu
        }

        /**
         * 成桂
         */
        data object Narikei : Reverse {

            override val moves: Set<Move> = Surface.Kin.moves

            override fun degeneracy(): Surface = Surface.Keima
        }

        /**
         * 成香
         */
        data object Narikyo : Reverse {

            override val moves: Set<Move> = Surface.Kin.moves

            override fun degeneracy(): Surface = Surface.Kyosya
        }

        /**
         * 成銀
         */
        data object Narigin : Reverse {

            override val moves: Set<Move> = Surface.Kin.moves

            override fun degeneracy(): Surface = Surface.Gin
        }

        /**
         * 馬
         */
        data object Uma : Reverse {

            override val moves: Set<Move> = setOf(
                Move.One.Front,
                Move.One.Back,
                Move.One.Left,
                Move.One.Right,
                Move.Endless.FrontLeft,
                Move.Endless.FrontRight,
                Move.Endless.BackLeft,
                Move.Endless.BackRight,
            )

            override fun degeneracy(): Surface = Surface.Kaku
        }

        /**
         * 竜
         */
        data object Ryu : Reverse {

            override val moves: Set<Move> = setOf(
                Move.Endless.Front,
                Move.Endless.Back,
                Move.Endless.Left,
                Move.Endless.Right,
                Move.One.FrontLeft,
                Move.One.FrontRight,
                Move.One.BackLeft,
                Move.One.BackRight,
            )

            override fun degeneracy(): Surface = Surface.Hisya
        }
    }
}
