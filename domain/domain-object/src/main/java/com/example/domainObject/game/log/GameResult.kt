package com.example.domainObject.game.log

import com.example.domainObject.game.rule.Turn

/**
 * 対局結果
 */
sealed interface GameResult {

  /**
   * 勝者
   *
   * @property turn 勝者の手番
   */
  data class Winner(val turn: Turn) : GameResult

  /**
   * 引き分け
   */
  data object Draw : GameResult
}
