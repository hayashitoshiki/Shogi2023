package com.example.domainObject.game.rule

/**
 * 盤上の設定
 *
 * @property blackHande 先手のハンデ
 * @property whiteHande 後手のハンデ
 */
data class BoardHandeRule(
    val blackHande: Hande,
    val whiteHande: Hande,
) {
    companion object {
        val DEFAULT = BoardHandeRule(
            blackHande = Hande.NON,
            whiteHande = Hande.NON,
        )
    }
}
