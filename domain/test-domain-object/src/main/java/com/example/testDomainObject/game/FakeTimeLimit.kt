package com.example.testDomainObject.game

import com.example.domainObject.game.game.Seconds
import com.example.domainObject.game.game.TimeLimit
import com.example.domainObject.game.rule.PlayerTimeLimitRule

fun TimeLimit.Companion.fake(
    setting: PlayerTimeLimitRule = PlayerTimeLimitRule.fake(),
    totalTime: Seconds = setting.totalTime,
    byoyomi: Seconds = setting.byoyomi,
): TimeLimit {
    return TimeLimit(
        setting = setting,
        totalTime = totalTime,
        byoyomi = byoyomi,
    )
}
