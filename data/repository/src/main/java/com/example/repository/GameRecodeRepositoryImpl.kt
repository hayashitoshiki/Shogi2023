package com.example.repository

import com.example.domainObject.game.log.GameRecode
import java.time.LocalDateTime

class GameRecodeRepositoryImpl: GameRecodeRepository {

  private val cash: MutableMap<LocalDateTime, GameRecode> = mutableMapOf()

  override fun set(recode: GameRecode) {
    cash[recode.date] = recode
  }

  override fun get(id: LocalDateTime): GameRecode? {
    return cash[id]
  }
}
