package com.example.testDomainObject.rule

import com.example.domainObject.game.rule.GameLogicRule

fun GameLogicRule.Companion.fake(
  tryRule: GameLogicRule.Rule.TryRule = GameLogicRule.Rule.TryRule.fake(),
  firstCheckEnd: GameLogicRule.Rule.FirstCheckEndRule = GameLogicRule.Rule.FirstCheckEndRule.fake(),
) = GameLogicRule(
  tryRule = tryRule,
  firstCheckEnd = firstCheckEnd,
)

fun GameLogicRule.Rule.TryRule.Companion.fake(
  blackRule: Boolean = true,
  whiteRule: Boolean = true,
) = GameLogicRule.Rule.TryRule(
  blackRule = blackRule,
  whiteRule = whiteRule,
)

fun GameLogicRule.Rule.FirstCheckEndRule.Companion.fake(
  blackRule: Boolean = false,
  whiteRule: Boolean = false,
) = GameLogicRule.Rule.FirstCheckEndRule(
  blackRule = blackRule,
  whiteRule = whiteRule,
)
