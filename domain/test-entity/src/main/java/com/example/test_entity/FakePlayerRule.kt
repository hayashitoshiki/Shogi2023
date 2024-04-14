package com.example.test_entity

import com.example.entity.game.rule.Hande
import com.example.entity.game.rule.PlayerRule

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