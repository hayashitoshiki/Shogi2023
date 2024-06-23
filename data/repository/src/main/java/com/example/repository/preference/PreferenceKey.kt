package com.example.repository.preference

/**
 * Preferenceアクセスキー
 */
class PreferenceKey {

    /**
     * Long型指定
     */
    enum class LongKey {
        TIME_LIMIT_BLACK_TOTAL_TIME,
        TIME_LIMIT_WHITE_TOTAL_TIME,
        TIME_LIMIT_BLACK_BYOYOMI,
        TIME_LIMIT_WHITE_BYOYOMI,
    }

    /**
     * Int型指定
     */
    enum class IntKey {
        SAMPLE_INT
    }

    /**
     * String型指定
     */
    enum class StringKey {
        SAMPLE_STRING,
    }
}

