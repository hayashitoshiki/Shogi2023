package com.example.testDomainObject.game

import com.example.domainObject.game.game.Second
import com.example.domainObject.game.game.TimeLimit
import com.example.domainObject.game.rule.TimeLimitRule

fun TimeLimit.Companion.fake(
    setting: TimeLimitRule = TimeLimitRule.fake(),
    totalTime: Second = Second.fake(600000),
    byoyomi: Second = Second.fake(),
): TimeLimit {
    return TimeLimit(
        setting = setting,
        totalTime = totalTime,
        byoyomi = byoyomi,
    )
}
