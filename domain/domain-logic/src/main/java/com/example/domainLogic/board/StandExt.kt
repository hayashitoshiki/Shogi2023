package com.example.domainLogic.board

import com.example.domainObject.game.board.Stand

/**
 * 持ち駒のセットアップ
 *
 * @return 設定内容に従った持ち駒の初期値
 */
fun Stand.Companion.setUp(): Stand {
    return Stand()
}
