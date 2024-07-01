package com.example.testDomainObject.rule

import com.example.domainObject.game.rule.Hande
import com.example.domainObject.game.rule.PlayerRule
import com.example.domainObject.game.rule.PlayerTimeLimitRule

fun PlayerRule.Companion.fake(
    hande: Hande = Hande.NON,
    playerTimeLimitRule: PlayerTimeLimitRule = PlayerTimeLimitRule.INIT,
    isFirstCheckEnd: Boolean = false,
    isTryRule: Boolean = false,
): PlayerRule {
    return PlayerRule(
        hande = hande,
        playerTimeLimitRule = playerTimeLimitRule,
        isFirstCheckEnd = isFirstCheckEnd,
        isTryRule = isTryRule,
    )
}
