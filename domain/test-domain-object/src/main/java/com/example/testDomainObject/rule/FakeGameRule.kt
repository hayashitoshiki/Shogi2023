package com.example.testDomainObject.rule

import com.example.domainObject.game.rule.BoardRule
import com.example.domainObject.game.rule.GameRule
import com.example.domainObject.game.rule.Hande
import com.example.domainObject.game.rule.PlayersRule

fun GameRule.Companion.fake(
    setUpRule: BoardRule.SetUpRule = BoardRule.SetUpRule.NORMAL,
    blackHande: Hande = Hande.NON,
    blackIsFirstCheckEnd: Boolean = false,
    blackIsTryRule: Boolean = false,
    whiteHande: Hande = Hande.NON,
    whiteIsFirstCheckEnd: Boolean = false,
    whiteIsTryRule: Boolean = false,
): GameRule {
    return GameRule(
        boardRule = BoardRule.fake(setUpRule),
        playersRule = PlayersRule.fake(
            blackHande = blackHande,
            blackIsFirstCheckEnd = blackIsFirstCheckEnd,
            blackIsTryRule = blackIsTryRule,
            whiteHande = whiteHande,
            whiteIsFirstCheckEnd = whiteIsFirstCheckEnd,
            whiteIsTryRule = whiteIsTryRule,
        ),
    )
}

fun GameRule.Companion.fake(
    setUpRule: BoardRule.SetUpRule = BoardRule.SetUpRule.NORMAL,
    playersRule: PlayersRule = PlayersRule.fake(),
): GameRule {
    return GameRule(
        boardRule = BoardRule.fake(setUpRule),
        playersRule = playersRule,
    )
}
