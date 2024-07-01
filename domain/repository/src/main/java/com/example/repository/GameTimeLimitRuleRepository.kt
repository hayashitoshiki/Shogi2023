package com.example.repository

import com.example.domainObject.game.rule.GameTimeLimitRule

/**
 * 対局の持ち時間設定リポジトリ
 */
interface GameTimeLimitRuleRepository {

  /**
   * 対局の持ち時間設定を設定
   *
   * @param gameTimeLimitRule 対局の持ち時間設定
   */
  fun set(gameTimeLimitRule: GameTimeLimitRule)

  /**
   * 対局の持ち時間設定を取得
   *
   * @return 対局の持ち時間設定
   */
  fun get(): GameTimeLimitRule
}
