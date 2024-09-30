package com.example.domainLogic.game

import com.example.domainObject.game.game.Seconds
import com.example.domainObject.game.game.TimeLimit

/**
 * 時間切れかどうかを判定
 *
 * @return 時間切れの場合true

 */
fun TimeLimit.isTimeOver(): Boolean = remainingTime == Seconds.ZERO

/**
 * 秒読みかどうかを判定
 *
 * @return 秒読みの場合true
 */
fun TimeLimit.isByoyomi(): Boolean = totalTime == Seconds.ZERO && byoyomi != Seconds.ZERO