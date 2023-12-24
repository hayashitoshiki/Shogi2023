package com.example.home.game.extension

import com.example.entity.game.piece.Piece
import com.example.entity.game.rule.Turn
import com.example.home.R

fun Piece.getIcon(turn: Turn): Int {
    return when (this) {
        Piece.Reverse.Narigin -> {
            when (turn) {
                Turn.Normal.Black -> R.drawable.ic_shogi_narigin_black
                Turn.Normal.White -> R.drawable.ic_shogi_narigin_white
            }
        }

        Piece.Reverse.Narikei -> {
            when (turn) {
                Turn.Normal.Black -> R.drawable.ic_shogi_narikei_black
                Turn.Normal.White -> R.drawable.ic_shogi_narikei_white
            }
        }

        Piece.Reverse.Narikyo -> {
            when (turn) {
                Turn.Normal.Black -> R.drawable.ic_shogi_narikyo_black
                Turn.Normal.White -> R.drawable.ic_shogi_narikyo_white
            }
        }

        Piece.Reverse.Ryu -> {
            when (turn) {
                Turn.Normal.Black -> R.drawable.ic_shogi_ryu_black
                Turn.Normal.White -> R.drawable.ic_shogi_ryu_white
            }
        }

        Piece.Reverse.To -> {
            when (turn) {
                Turn.Normal.Black -> R.drawable.ic_shogi_to_black
                Turn.Normal.White -> R.drawable.ic_shogi_to_white
            }
        }

        Piece.Reverse.Uma -> {
            when (turn) {
                Turn.Normal.Black -> R.drawable.ic_shogi_uma_black
                Turn.Normal.White -> R.drawable.ic_shogi_uma_white
            }
        }

        Piece.Surface.Fu -> {
            when (turn) {
                Turn.Normal.Black -> R.drawable.ic_shogi_fu_black
                Turn.Normal.White -> R.drawable.ic_shogi_fu_white
            }
        }

        Piece.Surface.Gin -> {
            when (turn) {
                Turn.Normal.Black -> R.drawable.ic_shogi_gin_black
                Turn.Normal.White -> R.drawable.ic_shogi_gin_white
            }
        }

        Piece.Surface.Gyoku -> R.drawable.ic_shogi_gyoku
        Piece.Surface.Hisya -> {
            when (turn) {
                Turn.Normal.Black -> R.drawable.ic_shogi_hisya_black
                Turn.Normal.White -> R.drawable.ic_shogi_hisya_white
            }
        }

        Piece.Surface.Kaku -> {
            when (turn) {
                Turn.Normal.Black -> R.drawable.ic_shogi_kaku_black
                Turn.Normal.White -> R.drawable.ic_shogi_kaku_white
            }
        }

        Piece.Surface.Keima -> {
            when (turn) {
                Turn.Normal.Black -> R.drawable.ic_shogi_keima_black
                Turn.Normal.White -> R.drawable.ic_shogi_keima_white
            }
        }

        Piece.Surface.Kin -> {
            when (turn) {
                Turn.Normal.Black -> R.drawable.ic_shogi_kin_black
                Turn.Normal.White -> R.drawable.ic_shogi_kin_white
            }
        }

        Piece.Surface.Kyosya -> {
            when (turn) {
                Turn.Normal.Black -> R.drawable.ic_shogi_kyosya_black
                Turn.Normal.White -> R.drawable.ic_shogi_kyosya_white
            }
        }

        Piece.Surface.Ou -> R.drawable.ic_shogi_ou
    }
}