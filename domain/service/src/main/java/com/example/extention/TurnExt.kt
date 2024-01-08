package com.example.extention

import com.example.entity.game.rule.Turn

/**
 * 手番交代
 *
 * @return 次の手番
 */
fun Turn.changeNextTurn(): Turn {
    return when (this) {
        Turn.Normal.Black -> Turn.Normal.White
        Turn.Normal.White -> Turn.Normal.Black
    }
}

/**
 * 相手の手番取得
 *
 * @return 相手の手番
 */
fun Turn.getOpponentTurn(): Turn {
    return when (this) {
        Turn.Normal.Black -> Turn.Normal.White
        Turn.Normal.White -> Turn.Normal.Black
    }
}
