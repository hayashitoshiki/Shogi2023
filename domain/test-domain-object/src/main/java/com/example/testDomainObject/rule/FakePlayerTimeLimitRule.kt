package com.example.testDomainObject.rule

import com.example.domainObject.game.game.Seconds
import com.example.domainObject.game.rule.PlayerTimeLimitRule

fun PlayerTimeLimitRule.Companion.fake(
    totalTime: Seconds = INIT.totalTime,
    byoyomi: Seconds = INIT.byoyomi,
): PlayerTimeLimitRule {
    return PlayerTimeLimitRule(
        totalTime = totalTime,
        byoyomi = byoyomi,
    )
}
