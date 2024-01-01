package com.example.repository.repositoryinterface

import com.example.entity.game.Log
import java.time.LocalDateTime

interface LogRepository {

    /**
     * 最新のキー取得
     *
     * @return ログ保存用のキー（まだ何も保存されていない場合は新規でKeyを作成して返却）
     */
    fun getLatestKey(): LocalDateTime

    /**
     * 新しい対局ログデータを作成
     *
     * @param key ログを保存するキー
     */
    fun createGameLog(key: LocalDateTime)

    /**
     * ログ追加
     *
     * @param key 追加対象の対局ログ
     * @param log 追加するログ
     */
    fun addLog(key: LocalDateTime, log: Log)

    /**
     * 対局ログに対して駒が成った時のログ更新
     *
     * @param key キー
     */
    fun fixLogByEvolution(key: LocalDateTime)

    /**
     * 指定した対局ログの取得
     *
     * @param key 取得した対局ログのキー
     * @return キー指定した対局ログ（ない場合はnull）
     */
    fun getGameLog(key: LocalDateTime): List<Log>?

    /**
     * 最も新しい対局ログを取得
     *
     * @return 最も新しい対局ログ（ない場合はnull）
     */
    fun getLatestLog(): List<Log>?
}
