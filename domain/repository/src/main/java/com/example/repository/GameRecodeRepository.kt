package com.example.repository

import com.example.domainObject.game.log.GameRecode
import java.time.LocalDateTime

/**
 * 対局ログ用Repository
 */
interface GameRecodeRepository {

  /**
   * 保存
   *
   * @param recode 保持する棋譜
   */
  fun set(recode: GameRecode)

  /**
   * 取得
   *
   * @return 棋譜
   */
  fun get(id: LocalDateTime): GameRecode?

  /**
   * 最新の棋譜を取得
   *
   * @return 棋譜
   */
  fun getLast(): GameRecode?
}