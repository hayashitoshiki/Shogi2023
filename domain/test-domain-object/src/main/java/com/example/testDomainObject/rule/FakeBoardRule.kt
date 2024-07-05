package com.example.testDomainObject.rule

import com.example.domainObject.game.rule.BoardHandeRule
import com.example.domainObject.game.rule.BoardRule

fun BoardRule.Companion.fake(
    setUpRule: BoardRule.SetUpRule = BoardRule.SetUpRule.NORMAL,
    boardHandeRule: BoardHandeRule = BoardHandeRule.DEFAULT,
): BoardRule {
    return BoardRule(
        setUpRule = setUpRule,
        boardHandeRule = boardHandeRule,
    )
}
