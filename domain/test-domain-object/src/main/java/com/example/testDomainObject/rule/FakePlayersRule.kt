package com.example.testDomainObject.rule

import com.example.domainObject.game.rule.Hande
import com.example.domainObject.game.rule.PlayerRule
import com.example.domainObject.game.rule.PlayersRule

fun PlayersRule.Companion.fake(
    blackHande: Hande = Hande.NON,
    blackIsFirstCheckEnd: Boolean = false,
    blackIsTryRule: Boolean = true,
    whiteHande: Hande = Hande.NON,
    whiteIsFirstCheckEnd: Boolean = false,
    whiteIsTryRule: Boolean = true,
): PlayersRule {
    return PlayersRule(
        blackRule = PlayerRule.fake(
            hande = blackHande,
            isFirstCheckEnd = blackIsFirstCheckEnd,
            isTryRule = blackIsTryRule,
        ),
        whiteRule = PlayerRule.fake(
            hande = whiteHande,
            isFirstCheckEnd = whiteIsFirstCheckEnd,
            isTryRule = whiteIsTryRule,
        ),
    )
}
