package com.example.home.ext

import androidx.annotation.StringRes
import com.example.entity.game.rule.Hande
import com.example.home.R

@get:StringRes
val Hande.stringRes: Int
    get() = when (this) {
        Hande.EIGHT -> R.string.hande_eight
        Hande.FOR -> R.string.hande_for
        Hande.SIX -> R.string.hande_six
        Hande.TWO -> R.string.hande_two
        Hande.HISYA -> R.string.hande_hisya
        Hande.KAKU -> R.string.hande_kaku
        Hande.NON -> R.string.hande_nothing
    }
