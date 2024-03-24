package com.example.home.ext

import androidx.annotation.DrawableRes
import com.example.entity.game.rule.Turn
import com.example.home.R

@get:DrawableRes
val Turn.turnImageRes: Int
    get() = when (this) {
        Turn.Normal.Black -> R.drawable.ic_piece_black
        Turn.Normal.White -> R.drawable.ic_piece_white
    }