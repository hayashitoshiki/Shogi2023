package com.example.domainLogic.rule

import com.example.domainObject.game.rule.Turn

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