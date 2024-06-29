package com.example.usecaseinterface.usecase

import com.example.domainObject.game.rule.GameRule

/**
 * 将棋のビジネスロジック
 *
 */
interface HomeUseCase {

    /**
     * 対局ルールセット
     *
     * @param rule ルール
     */
    fun setGameRule(rule: GameRule)
}
