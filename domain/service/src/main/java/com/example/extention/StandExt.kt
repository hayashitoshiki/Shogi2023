package com.example.extention

import com.example.entity.game.board.Stand
import com.example.entity.game.rule.Hande
import com.example.entity.game.rule.PlayerRule

/**
 * 持ち駒のセットアップ
 *
 * @param playerRule 設定内容
 * @return 設定内容に従った持ち駒の初期値
 */
fun Stand.Companion.setUp(playerRule: PlayerRule): Stand {
    return when (playerRule.hande) {
        Hande.NON,
        Hande.KAKU,
        Hande.HISYA,
        Hande.TWO,
        Hande.FOR,
        Hande.SIX,
        Hande.EIGHT -> Stand()
    }
}
