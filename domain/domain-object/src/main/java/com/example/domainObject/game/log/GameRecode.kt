package com.example.domainObject.game.log

import com.example.domainObject.game.rule.GameRule
import java.time.LocalDateTime

/**
 * 対局ログ
 *
 * @property date 対局日時
 * @property result 対局結果
 * @property rule 対局ルール
 * @property moveRecodes 手番ごとの対局ログ
 */
data class GameRecode(
  val date: LocalDateTime,
  val result: GameResult,
  val rule: GameRule,
  val moveRecodes: List<MoveRecode>,
)
