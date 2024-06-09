package com.example.testDomainObject.rule

import com.example.domainObject.game.rule.BoardRule

fun BoardRule.Companion.fake(
    setUpRule: BoardRule.SetUpRule = BoardRule.SetUpRule.NORMAL
): BoardRule {
    return BoardRule(
        setUpRule = setUpRule,
    )
}
