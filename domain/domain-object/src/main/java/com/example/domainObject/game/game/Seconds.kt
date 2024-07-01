package com.example.domainObject.game.game

/**
 * 秒数
 *
 * @property value 秒数
 */
@JvmInline
value class Seconds private constructor(private val value: Long) {
    init {
        require(value >= 0) { "Time must be non-negative value = $value" }
    }

    /**
     * 分数
     */
    val minutes: Long
        get() = value / 60

    /**
     * 秒数
     */
    val seconds: Long
        get() = value % 60

    /**
     * ミリ秒数
     */
    val millisecond: Long
        get() = value * 1000

    companion object {

        val ZERO = Seconds(0)

        /**
         * 分数で設定
         *
         * @param value 分数
         * @return 秒数オブジェクト
         */
        fun setMinutes(value: Long): Seconds {
            return Seconds(value * 60)
        }

        /**
         * 秒数で設定
         *
         * @param value 秒数
         * @return 秒数オブジェクト
         */
        fun setSeconds(value: Long): Seconds {
            return Seconds(value)
        }

        /**
         * ミリ秒数で設定
         *
         * @param value ミリ秒数
         * @return 秒数オブジェクト
         */
        fun setMillisecond(value: Long): Seconds {
            return Seconds(value / 1000)
        }
    }
}
