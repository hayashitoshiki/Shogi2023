package com.example.testrepository

import com.example.domainObject.game.rule.GameTimeLimitRule
import com.example.domainObject.game.rule.PlayerTimeLimitRule
import com.example.repository.GameTimeLimitRuleRepository
import com.example.testDomainObject.rule.fake

class FakeGameTimeLimitRuleRepository: GameTimeLimitRuleRepository {

  var callSetCount = 0
    private set
  var callGetCount = 0
    private set

  var setGameRuleLogic: (GameTimeLimitRule) -> Unit = { }
  var getGameRuleLogic: () -> GameTimeLimitRule = {
    GameTimeLimitRule(
      blackTimeLimitRule = PlayerTimeLimitRule.fake(),
      whiteTimeLimitRule = PlayerTimeLimitRule.fake(),
    )
  }

  override fun set(gameTimeLimitRule: GameTimeLimitRule) {
    callSetCount += 1
    setGameRuleLogic(gameTimeLimitRule)
  }

  override fun get(): GameTimeLimitRule{
    callGetCount += 1
    return getGameRuleLogic()
  }
}