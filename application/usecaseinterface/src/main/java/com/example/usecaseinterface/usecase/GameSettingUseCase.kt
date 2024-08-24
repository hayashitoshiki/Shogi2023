package com.example.usecaseinterface.usecase

import com.example.domainObject.game.rule.GameRule
import com.example.domainObject.game.rule.GameTimeLimitRule

/**
 * 将棋の設定に関するビジネスロジック
 *
 */
interface GameSettingUseCase {

    /**
     * 対局の持ち時間設定を取得
     *
     * @return 対局の持ち時間設定
     */
    suspend fun getTimeLimits(): GameTimeLimitRule

    /**
     * 対局ルールセット
     *
     * @param rule ルール
     */
    suspend fun setGameRule(rule: GameRule)
}
