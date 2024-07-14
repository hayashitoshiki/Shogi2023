package com.example.testDomainObject.rule

import com.example.domainObject.game.rule.GameTimeLimitRule
import com.example.domainObject.game.rule.PlayerTimeLimitRule

fun GameTimeLimitRule.Companion.fake(
    blackTimeLimitRule: PlayerTimeLimitRule = PlayerTimeLimitRule.fake(),
    whiteTimeLimitRule: PlayerTimeLimitRule = PlayerTimeLimitRule.fake(),
) = GameTimeLimitRule(
    blackTimeLimitRule = blackTimeLimitRule,
    whiteTimeLimitRule = whiteTimeLimitRule,
)
