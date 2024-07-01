package com.example.testDomainObject.rule

import com.example.domainObject.game.rule.Hande
import com.example.domainObject.game.rule.PlayerRule
import com.example.domainObject.game.rule.TimeLimitRule

fun PlayerRule.Companion.fake(
    hande: Hande = Hande.NON,
    timeLimitRule: TimeLimitRule = TimeLimitRule.INIT,
    isFirstCheckEnd: Boolean = false,
    isTryRule: Boolean = false,
): PlayerRule {
    return PlayerRule(
        hande = hande,
        timeLimitRule = timeLimitRule,
        isFirstCheckEnd = isFirstCheckEnd,
        isTryRule = isTryRule,
    )
}
