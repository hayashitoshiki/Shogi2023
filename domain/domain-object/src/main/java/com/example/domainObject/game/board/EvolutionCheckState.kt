package com.example.domainObject.game.board


/**
 * 成ることができるか判別するステータス
 *
 */
enum class EvolutionCheckState {
    /**
     * 強制的に成る
     *
     */
    Should,

    /**
     * 成れない
     *
     */
    No,

    /**
     * 選択できる
     *
     */
    Choose;
}
