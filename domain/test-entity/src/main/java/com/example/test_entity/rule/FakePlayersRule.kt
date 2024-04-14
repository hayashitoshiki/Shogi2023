package com.example.test_entity.rule

import com.example.entity.game.rule.Hande
import com.example.entity.game.rule.PlayerRule
import com.example.entity.game.rule.PlayersRule

fun PlayersRule.Companion.fake(
    blackHande: Hande = Hande.NON,
    blackIsFirstCheckEnd: Boolean = false,
    blackIsTryRule: Boolean = false,
    whiteHande: Hande = Hande.NON,
    whiteIsFirstCheckEnd: Boolean = false,
    whiteIsTryRule: Boolean = false,
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
