package com.example.repository.repository

import com.example.domainObject.game.Log
import com.example.repository.repositoryinterface.LogRepository
import java.time.LocalDateTime
import javax.inject.Inject

/**
 * 対局ログ保持用Repository
 *
 */
class LogRepositoryImpl @Inject constructor() : LogRepository {

    private val logCash: MutableMap<LocalDateTime, List<Log>> = mutableMapOf()

    override fun getLatestKey(): LocalDateTime {
        return logCash.keys.maxByOrNull { it } ?: return LocalDateTime.now()
    }

    override fun createGameLog(key: LocalDateTime) {
        logCash[key] = emptyList()
    }

    override fun addLog(key: LocalDateTime, log: Log) {
        val logs = logCash[key]?.toMutableList() ?: return
        logs.add(log)
        logCash[key] = logs
    }

    override fun fixLogByEvolution(key: LocalDateTime) {
        val logs = logCash[key]?.toMutableList() ?: return
        logs[logs.size - 1] = logs.last().copy(isEvolution = true)
        logCash[key] = logs
    }

    override fun getGameLog(key: LocalDateTime): List<Log>? {
        return logCash[key]
    }

    override fun getLatestLog(): List<Log>? {
        val key = logCash.keys.maxByOrNull { it } ?: return null
        return logCash[key]
    }
}
