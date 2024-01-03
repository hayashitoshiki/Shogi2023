package com.example.entity.game.rule

import com.example.entity.game.board.CellStatus
import com.example.entity.game.board.Position
import com.example.entity.game.board.Size
import com.example.entity.game.piece.Piece

/**
 * 盤面初期設定
 *
 * 盤面のサイズと駒落ちを設定
 */
sealed interface PieceSetUpRule {

    /**
     * 将棋盤のサイズ
     */
    val boardSize: Size

    /**
     * 初期化時の駒一覧
     */
    val initPiece: Map<Position, CellStatus>

    /**
     * 通常モード
     *
     */
    sealed interface Normal : PieceSetUpRule {

        companion object {
            private val blackHisya = Pair(
                Position(2, 8),
                CellStatus.Fill.FromPiece(Piece.Surface.Hisya, Turn.Normal.Black),
            )
            private val blackKaku = Pair(
                Position(8, 8),
                CellStatus.Fill.FromPiece(Piece.Surface.Kaku, Turn.Normal.Black),
            )
            private val blackKyo = mapOf(
                Pair(
                    Position(1, 9),
                    CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, Turn.Normal.Black),
                ),
                Pair(
                    Position(9, 9),
                    CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, Turn.Normal.Black),
                ),
            )
            private val blackKei = mapOf(
                Pair(
                    Position(2, 9),
                    CellStatus.Fill.FromPiece(Piece.Surface.Keima, Turn.Normal.Black),
                ),
                Pair(
                    Position(8, 9),
                    CellStatus.Fill.FromPiece(Piece.Surface.Keima, Turn.Normal.Black),
                ),
            )
            private val blackGin = mapOf(
                Pair(
                    Position(3, 9),
                    CellStatus.Fill.FromPiece(Piece.Surface.Gin, Turn.Normal.Black),
                ),
                Pair(
                    Position(7, 9),
                    CellStatus.Fill.FromPiece(Piece.Surface.Gin, Turn.Normal.Black),
                ),
            )
            private val blackKin = setOf(
                Pair(
                    Position(4, 9),
                    CellStatus.Fill.FromPiece(Piece.Surface.Kin, Turn.Normal.Black)
                ),
                Pair(
                    Position(6, 9),
                    CellStatus.Fill.FromPiece(Piece.Surface.Kin, Turn.Normal.Black)
                ),
            )
            private val whiteHisya = Pair(
                Position(8, 2),
                CellStatus.Fill.FromPiece(Piece.Surface.Hisya, Turn.Normal.White),
            )
            private val whiteKaku = Pair(
                Position(2, 2),
                CellStatus.Fill.FromPiece(Piece.Surface.Kaku, Turn.Normal.White),
            )
            private val whiteKyo = mapOf(
                Pair(
                    Position(1, 1),
                    CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, Turn.Normal.White),
                ),
                Pair(
                    Position(9, 1),
                    CellStatus.Fill.FromPiece(Piece.Surface.Kyosya, Turn.Normal.White),
                ),
            )
            private val whiteKei = mapOf(
                Pair(
                    Position(2, 1),
                    CellStatus.Fill.FromPiece(Piece.Surface.Keima, Turn.Normal.White),
                ),
                Pair(
                    Position(8, 1),
                    CellStatus.Fill.FromPiece(Piece.Surface.Keima, Turn.Normal.White),
                ),
            )
            private val whiteGin = mapOf(
                Pair(
                    Position(3, 1),
                    CellStatus.Fill.FromPiece(Piece.Surface.Gin, Turn.Normal.White),
                ),
                Pair(
                    Position(7, 1),
                    CellStatus.Fill.FromPiece(Piece.Surface.Gin, Turn.Normal.White),
                ),
            )
            private val whiteKin = setOf(
                Pair(
                    Position(4, 1),
                    CellStatus.Fill.FromPiece(Piece.Surface.Kin, Turn.Normal.White)
                ),
                Pair(
                    Position(6, 1),
                    CellStatus.Fill.FromPiece(Piece.Surface.Kin, Turn.Normal.White)
                ),
            )
        }

        override val boardSize: Size get() = Size(9, 9)

        /**
         * 平手
         */
        data object NoHande : Normal {
            override val initPiece: Map<Position, CellStatus> = mutableMapOf(
                Position(5, 1) to CellStatus.Fill.FromPiece(Piece.Surface.Ou, Turn.Normal.White),
                Position(1, 3) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.White),
                Position(2, 3) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.White),
                Position(3, 3) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.White),
                Position(4, 3) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.White),
                Position(5, 3) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.White),
                Position(6, 3) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.White),
                Position(7, 3) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.White),
                Position(8, 3) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.White),
                Position(9, 3) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.White),
                Position(5, 9) to CellStatus.Fill.FromPiece(Piece.Surface.Gyoku, Turn.Normal.Black),
                Position(1, 7) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black),
                Position(2, 7) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black),
                Position(3, 7) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black),
                Position(4, 7) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black),
                Position(5, 7) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black),
                Position(6, 7) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black),
                Position(7, 7) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black),
                Position(8, 7) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black),
                Position(9, 7) to CellStatus.Fill.FromPiece(Piece.Surface.Fu, Turn.Normal.Black),
            )
                .plus(whiteKyo)
                .plus(whiteKei)
                .plus(whiteGin)
                .plus(whiteKin)
                .plus(whiteHisya)
                .plus(whiteKaku)
                .plus(blackKyo)
                .plus(blackKei)
                .plus(blackGin)
                .plus(blackKin)
                .plus(blackHisya)
                .plus(blackKaku)
        }

        /**
         * 先手飛車落ち
         */
        data object BlackHandeHisya : Normal {
            override val initPiece: Map<Position, CellStatus>
                get() = NoHande.initPiece - blackHisya.first
        }

        /**
         * 先手角落ち
         */
        data object BlackHandeKaku : Normal {
            override val initPiece: Map<Position, CellStatus>
                get() = NoHande.initPiece - blackKaku.first
        }

        /**
         * 先手２枚落ち
         */
        data object BlackHandeHisyaKaku : Normal {
            override val initPiece: Map<Position, CellStatus>
                get() = NoHande.initPiece - setOf(blackHisya.first, blackKaku.first)
        }

        /**
         * 先手４枚落ち
         */
        data object BlackHandeFor : Normal {
            override val initPiece: Map<Position, CellStatus>
                get() = BlackHandeHisyaKaku.initPiece - blackKyo.entries.map { it.key }.toSet()
        }

        /**
         * 先手6枚落ち
         */
        data object BlackHandeSix : Normal {
            override val initPiece: Map<Position, CellStatus>
                get() = BlackHandeFor.initPiece - blackKei.entries.map { it.key }.toSet()
        }

        /**
         * 先手８枚落ち
         */
        data object BlackHandeEight : Normal {
            override val initPiece: Map<Position, CellStatus>
                get() = BlackHandeSix.initPiece - blackGin.map { it.key }.toSet()
        }

        /**
         * 後手飛車落ち
         */
        data object WhiteHandeHisya : Normal {
            override val initPiece: Map<Position, CellStatus>
                get() = NoHande.initPiece - whiteHisya.first
        }

        /**
         * 後手角落ち
         */
        data object WhiteHandeKaku : Normal {
            override val initPiece: Map<Position, CellStatus>
                get() = NoHande.initPiece - whiteKaku.first
        }

        /**
         * 後手２枚落ち
         */
        data object WhiteHandeHisyaKaku : Normal {
            override val initPiece: Map<Position, CellStatus>
                get() = NoHande.initPiece - setOf(whiteHisya.first, whiteKaku.first)
        }

        /**
         * 後手４枚落ち
         */
        data object WhiteHandeFor : Normal {
            override val initPiece: Map<Position, CellStatus>
                get() = WhiteHandeHisyaKaku.initPiece - whiteKyo.entries.map { it.key }.toSet()
        }

        /**
         * 後手6枚落ち
         */
        data object WhiteHandeSix : Normal {
            override val initPiece: Map<Position, CellStatus>
                get() = WhiteHandeFor.initPiece - whiteKei.entries.map { it.key }.toSet()
        }

        /**
         * 後手８枚落ち
         */
        data object WhiteHandeEight : Normal {
            override val initPiece: Map<Position, CellStatus>
                get() = WhiteHandeSix.initPiece - whiteGin.map { it.key }.toSet()
        }
    }
}
