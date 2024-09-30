package com.example.usecaseinterface.model

import com.example.domainObject.game.rule.Turn

sealed class TimeOverUseCaseModel{
  data object None: TimeOverUseCaseModel()

  data class TimeOver(val turn: Turn): TimeOverUseCaseModel()
}