package com.example.domainObject.game.rule

/**
 * 対局のロジックルール
 *
 * @property tryRule トライルール
 * @property firstCheckEnd 王手将棋ルール
 */
data class GameLogicRule(
    val tryRule: Rule.TryRule,
    val firstCheckEnd: Rule.FirstCheckEndRule,
) {

    /**
     * 対局ルールの基底クラス
     *
     * @property blackRule 先手のルール
     * @property whiteRule 後手のルール
     */
    sealed interface Rule {
        val blackRule: Boolean
        val whiteRule: Boolean

        /**
         * トライルール
         */
        data class TryRule(
            override val blackRule: Boolean,
            override val whiteRule: Boolean,
        ) : Rule {
            companion object {

                val DEFAULT = TryRule(
                    blackRule = true,
                    whiteRule = true,
                )

                fun set(isTry: Boolean): TryRule {
                    return TryRule(
                        blackRule = isTry,
                        whiteRule = isTry,
                    )
                }
            }
        }

        /**
         * 王手将棋ルール
         */
        data class FirstCheckEndRule(
            override val blackRule: Boolean,
            override val whiteRule: Boolean,
        ) : Rule {
            companion object {
                val DEFAULT = FirstCheckEndRule(
                    blackRule = false,
                    whiteRule = false,
                )

                fun set(isFirstCheckEnd: Boolean): FirstCheckEndRule {
                    return FirstCheckEndRule(
                        blackRule = isFirstCheckEnd,
                        whiteRule = isFirstCheckEnd,
                    )
                }
            }
        }
    }

    companion object {
        val DEFAULT = GameLogicRule(
            tryRule = Rule.TryRule.DEFAULT,
            firstCheckEnd = Rule.FirstCheckEndRule.DEFAULT,
        )
    }
}
