package com.example.testDomainObject.rule

import com.example.domainObject.game.rule.BoardHandeRule
import com.example.domainObject.game.rule.Hande

fun BoardHandeRule.Companion.fake(
  blackHande: Hande = Hande.NON,
  whiteHande: Hande = Hande.NON,
) = BoardHandeRule(
  blackHande = blackHande,
  whiteHande = whiteHande,
)