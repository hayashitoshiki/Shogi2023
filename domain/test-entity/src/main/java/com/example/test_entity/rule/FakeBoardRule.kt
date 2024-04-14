package com.example.test_entity.rule

import com.example.entity.game.rule.BoardRule

fun BoardRule.Companion.fake(
    setUpRule: BoardRule.SetUpRule = BoardRule.SetUpRule.NORMAL
): BoardRule {
    return BoardRule(
        setUpRule = setUpRule,
    )
}
