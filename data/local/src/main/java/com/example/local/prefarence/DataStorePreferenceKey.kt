package com.example.local.prefarence

import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.longPreferencesKey
import com.example.domainObject.game.rule.PlayerTimeLimitRule

/**
 * DataStorePreferenceアクセスキー
 *
 * @param T アクセスキーの型
 * @property rawKey DataStoreのキー
 * @property defaultValue デフォルト値
 */
sealed class DataStorePreferenceKey<T : Any>(val rawKey: Preferences.Key<T>, val defaultValue: T) {

    /**
     * 先手の切れ負け設定時間
     */
    data object TimeLimitBlackTotalTime : DataStorePreferenceKey<Long>(
        longPreferencesKey("time_limit_black_total_time"),
        PlayerTimeLimitRule.INIT.totalTime.millisecond,
    )

    /**
     * 後手の切れ負け設定時間
     */
    data object TimeLimitWhiteTotalTime : DataStorePreferenceKey<Long>(
        longPreferencesKey("time_limit_white_total_time"),
        PlayerTimeLimitRule.INIT.totalTime.millisecond,
    )

    /**
     * 先手の秒読み設定時間
     */
    data object TimeLimitBlackByoyomi : DataStorePreferenceKey<Long>(
        longPreferencesKey("time_limit_black_byoyomi"),
        PlayerTimeLimitRule.INIT.totalTime.seconds,
    )

    /**
     * 後手の秒読み設定時間
     */
    data object TimeLimitWhiteByoyomi : DataStorePreferenceKey<Long>(
        longPreferencesKey("time_limit_white_byoyomi"),
        PlayerTimeLimitRule.INIT.totalTime.seconds,
    )

    companion object {
        const val DEFAULT_VALUE_STRING = ""
        const val DEFAULT_VALUE_INT = 0
        const val DEFAULT_VALUE_LONG = 0L
    }
}
