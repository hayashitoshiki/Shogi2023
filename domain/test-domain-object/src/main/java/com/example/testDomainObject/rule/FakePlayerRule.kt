package com.example.testDomainObject.rule

import com.example.domainObject.game.rule.Hande
import com.example.domainObject.game.rule.PlayerRule

fun PlayerRule.Companion.fake(
    hande: Hande = Hande.NON,
    isFirstCheckEnd: Boolean = false,
    isTryRule: Boolean = false,
): PlayerRule {
    return PlayerRule(
        hande = hande,
        isFirstCheckEnd = isFirstCheckEnd,
        isTryRule = isTryRule,
    )

}