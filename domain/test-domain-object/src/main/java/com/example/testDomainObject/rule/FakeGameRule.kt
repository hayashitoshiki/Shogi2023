package com.example.testDomainObject.rule

import com.example.domainObject.game.rule.BoardRule
import com.example.domainObject.game.rule.GameLogicRule
import com.example.domainObject.game.rule.GameRule
import com.example.domainObject.game.rule.GameTimeLimitRule

fun GameRule.Companion.fake(
    setUpRule: BoardRule.SetUpRule = BoardRule.SetUpRule.NORMAL,
    logicRule: GameLogicRule = GameLogicRule.fake(),
    timeLimitRule: GameTimeLimitRule = GameTimeLimitRule.fake(),
): GameRule {
    return GameRule(
        boardRule = BoardRule.fake(setUpRule),
        logicRule = logicRule,
        timeLimitRule = timeLimitRule,
    )
}

fun GameRule.Companion.fakeFromLogicRuleFirstCheckEnd(
    blackRule: Boolean = false,
    whiteRule: Boolean = false,
): GameRule = GameRule.fake(
    logicRule = GameLogicRule.fake(
        firstCheckEnd = GameLogicRule.Rule.FirstCheckEndRule.fake(
            blackRule = blackRule,
            whiteRule = whiteRule,
        ),
    ),
)

fun GameRule.Companion.fakeFromLogicRuleTryRule(
    blackRule: Boolean = true,
    whiteRule: Boolean = true,
): GameRule = GameRule.fake(
    logicRule = GameLogicRule.fake(
        tryRule = GameLogicRule.Rule.TryRule.fake(
            blackRule = blackRule,
            whiteRule = whiteRule,
        ),
    ),
)
