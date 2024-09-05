package com.example.repository

import com.example.domainObject.game.log.GameRecode

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
  fun get(): GameRecode
}