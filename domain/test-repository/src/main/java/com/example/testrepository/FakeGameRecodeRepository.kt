package com.example.testrepository

import com.example.domainObject.game.log.GameRecode
import com.example.repository.GameRecodeRepository
import java.time.LocalDateTime

class FakeGameRecodeRepository: GameRecodeRepository {

  var callSetCount = 0
    private set
  var callGetCount = 0
    private set
  var callGetLastCount = 0
    private set

  var setLogic: (recode: GameRecode) -> Unit = { }
  var getLogic: (id: LocalDateTime) -> GameRecode? = { null }
  var getLastLogic: () -> GameRecode? = { null }

  override fun set(recode: GameRecode) {
    callSetCount += 1
    setLogic(recode)
  }

  override fun get(id: LocalDateTime): GameRecode? {
    callGetCount += 1
    return getLogic(id)
  }

  override fun getLast(): GameRecode? {
    callGetLastCount += 1
    return getLastLogic()
  }
}