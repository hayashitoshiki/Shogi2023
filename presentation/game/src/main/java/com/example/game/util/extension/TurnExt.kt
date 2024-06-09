package com.example.game.util.extension

import com.example.domainObject.game.rule.Turn
import com.example.game.R


val Turn.stringRes: Int
    get() = when (this) {
        Turn.Normal.Black -> R.string.turn_black_name
        Turn.Normal.White -> R.string.turn_white_name
    }

