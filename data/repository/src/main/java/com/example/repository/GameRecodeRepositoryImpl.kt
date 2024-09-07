package com.example.repository

import com.example.domainObject.game.log.GameRecode
import java.time.LocalDateTime
import javax.inject.Inject

class GameRecodeRepositoryImpl @Inject constructor() : GameRecodeRepository {

  private val cash: MutableMap<LocalDateTime, GameRecode> = mutableMapOf()

  override fun set(recode: GameRecode) {
    cash[recode.date] = recode
  }

  override fun get(id: LocalDateTime): GameRecode? {
    return cash[id]
  }

  override fun getLast(): GameRecode? {
    return cash.maxByOrNull { it.key }?.value
  }
}
