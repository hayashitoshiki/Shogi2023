package com.example.repository.preference

/**
 * Preferenceアクセスキー
 */
sealed class PreferenceKey <T: Any>(val rawKey: String) {

    data object TIME_LIMIT_BLACK_TOTAL_TIME: PreferenceKey<Long>("time_limit_black_total_time")
    data object TIME_LIMIT_WHITE_TOTAL_TIME: PreferenceKey<Long>("time_limit_white_total_time")
    data object TIME_LIMIT_BLACK_BYOYOMI: PreferenceKey<Long>("time_limit_black_byoyomi")
    data object TIME_LIMIT_WHITE_BYOYOMI: PreferenceKey<Long>("time_limit_white_byoyomi")
    data object TIME_LIMIT_BLACK_TOTAL_TIME2: PreferenceKey<Long>("time_limit_black_total_time2")
    data object TIME_LIMIT_WHITE_TOTAL_TIME2: PreferenceKey<Long>("time_limit_white_total_time2")
    data object TIME_LIMIT_BLACK_BYOYOMI2: PreferenceKey<Long>("time_limit_black_byoyomi2")
    data object TIME_LIMIT_WHITE_BYOYOMI2: PreferenceKey<Long>("time_limit_white_byoyomi2")

    data class MIGRATION_COMPLETE_KEY(val version: String): PreferenceKey<String>("migration_complete_key" + version)

    companion object {
        const val VERSION1 = "1"
        const val VERSION2 = "2"

        val migrationKeys = mapOf(
            TIME_LIMIT_BLACK_TOTAL_TIME to TIME_LIMIT_BLACK_TOTAL_TIME2,
            TIME_LIMIT_WHITE_TOTAL_TIME to TIME_LIMIT_WHITE_TOTAL_TIME2,
            TIME_LIMIT_BLACK_BYOYOMI to TIME_LIMIT_BLACK_BYOYOMI2,
            TIME_LIMIT_WHITE_BYOYOMI to TIME_LIMIT_WHITE_BYOYOMI2,
        )
    }
}
