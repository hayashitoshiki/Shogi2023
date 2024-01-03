package com.example.home.ext

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.entity.game.rule.PieceSetUpRule
import com.example.home.R

@get:StringRes
val PieceSetUpRule.Normal.stringRes: Int
    get() = when (this) {
        PieceSetUpRule.Normal.BlackHandeEight -> R.string.hande_eight
        PieceSetUpRule.Normal.BlackHandeFor -> R.string.hande_for
        PieceSetUpRule.Normal.BlackHandeHisya -> R.string.hande_hisya
        PieceSetUpRule.Normal.BlackHandeHisyaKaku -> R.string.hande_two
        PieceSetUpRule.Normal.BlackHandeKaku -> R.string.hande_kaku
        PieceSetUpRule.Normal.BlackHandeSix -> R.string.hande_six
        PieceSetUpRule.Normal.NoHande -> R.string.hande_nothing
        PieceSetUpRule.Normal.WhiteHandeEight -> R.string.hande_eight
        PieceSetUpRule.Normal.WhiteHandeFor -> R.string.hande_for
        PieceSetUpRule.Normal.WhiteHandeHisya -> R.string.hande_hisya
        PieceSetUpRule.Normal.WhiteHandeHisyaKaku -> R.string.hande_two
        PieceSetUpRule.Normal.WhiteHandeKaku -> R.string.hande_kaku
        PieceSetUpRule.Normal.WhiteHandeSix -> R.string.hande_six
    }

@get:DrawableRes
val PieceSetUpRule.Normal.turnImageRes: Int?
    get() = when (this) {
        PieceSetUpRule.Normal.BlackHandeEight,
        PieceSetUpRule.Normal.BlackHandeFor,
        PieceSetUpRule.Normal.BlackHandeHisya,
        PieceSetUpRule.Normal.BlackHandeHisyaKaku,
        PieceSetUpRule.Normal.BlackHandeKaku,
        PieceSetUpRule.Normal.BlackHandeSix -> R.drawable.ic_piece_black

        PieceSetUpRule.Normal.WhiteHandeEight,
        PieceSetUpRule.Normal.WhiteHandeFor,
        PieceSetUpRule.Normal.WhiteHandeHisya,
        PieceSetUpRule.Normal.WhiteHandeHisyaKaku,
        PieceSetUpRule.Normal.WhiteHandeKaku,
        PieceSetUpRule.Normal.WhiteHandeSix -> R.drawable.ic_piece_white

        PieceSetUpRule.Normal.NoHande -> null
    }
