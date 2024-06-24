package com.example.testDomainObject.game

import com.example.domainObject.game.game.Second
import com.example.domainObject.game.rule.TimeLimitRule

fun TimeLimitRule.Companion.fake(
    totalTime: Second = Second.fake(),
    byoyomi: Second = Second.fake(),
): TimeLimitRule {
    return TimeLimitRule(
        totalTime = totalTime,
        byoyomi = byoyomi,
    )
}