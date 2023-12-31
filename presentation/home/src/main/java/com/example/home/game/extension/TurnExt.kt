package com.example.home.game.extension

import com.example.entity.game.rule.Turn
import com.example.home.R


val Turn.stringRes: Int
    get() = when (this) {
        Turn.Normal.Black -> R.string.turn_black_name
        Turn.Normal.White -> R.string.turn_white_name
    }

