package com.example.repository

import com.example.domainObject.game.game.Seconds
import com.example.domainObject.game.rule.GameTimeLimitRule
import com.example.domainObject.game.rule.PlayerTimeLimitRule
import com.example.local.prefarence.DataStorePreferenceKey
import com.example.local.prefarence.DataStorePreferenceManager
import javax.inject.Inject

class GameTimeLimitRuleRepositoryImpl @Inject constructor(
    private val dataStorePreferenceManager: DataStorePreferenceManager,
) : GameTimeLimitRuleRepository {

    override suspend fun set(gameTimeLimitRule: GameTimeLimitRule) {
        mapOf(
            DataStorePreferenceKey.TimeLimitWhiteTotalTime to gameTimeLimitRule.whiteTimeLimitRule.totalTime.millisecond,
            DataStorePreferenceKey.TimeLimitWhiteByoyomi to gameTimeLimitRule.whiteTimeLimitRule.byoyomi.millisecond,
            DataStorePreferenceKey.TimeLimitBlackTotalTime to gameTimeLimitRule.blackTimeLimitRule.totalTime.millisecond,
            DataStorePreferenceKey.TimeLimitBlackByoyomi to gameTimeLimitRule.blackTimeLimitRule.byoyomi.millisecond,
        ).forEach { (key, value) ->
            dataStorePreferenceManager.set(key, value)
        }
    }

    override suspend fun get(): GameTimeLimitRule {
        val whiteTotalTime = getCashTimeLimitSeconds(DataStorePreferenceKey.TimeLimitWhiteTotalTime)
        val whiteByoyomi = getCashTimeLimitSeconds(DataStorePreferenceKey.TimeLimitWhiteByoyomi)
        val blackTotalTime = getCashTimeLimitSeconds(DataStorePreferenceKey.TimeLimitBlackTotalTime)
        val blackByoyomi = getCashTimeLimitSeconds(DataStorePreferenceKey.TimeLimitBlackByoyomi)
        return GameTimeLimitRule(
            whiteTimeLimitRule = PlayerTimeLimitRule(
                totalTime = whiteTotalTime,
                byoyomi = whiteByoyomi,
            ),
            blackTimeLimitRule = PlayerTimeLimitRule(
                totalTime = blackTotalTime,
                byoyomi = blackByoyomi,
            ),
        )
    }

    private suspend fun getCashTimeLimitSeconds(key: DataStorePreferenceKey<Long>): Seconds {
        val cash = dataStorePreferenceManager.get(key)
        return Seconds.setMillisecond(cash)
    }
}
