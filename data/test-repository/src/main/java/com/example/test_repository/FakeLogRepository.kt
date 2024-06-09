package com.example.test_repository

import com.example.domainObject.game.Log
import com.example.repository.repositoryinterface.LogRepository
import java.time.LocalDateTime

class FakeLogRepository : LogRepository {

    var callGetLatestKeyCount = 0
        private set
    var callCreateGameLogCount = 0
        private set
    var callAddLogCount = 0
        private set
    var callFixLogByEvolutionCount = 0
        private set
    var callGetGameLogCount = 0
        private set
    var callGetLatestLogCount = 0
        private set

    var getLatestKeyLogic: () -> LocalDateTime = { LocalDateTime.now() }
    var createGameLogLogic: (LocalDateTime) -> Unit = { }
    var addLogLogic: (key: LocalDateTime, log: Log) -> Unit = { _, _ -> }
    var fixLogByEvolutionLogic: (key: LocalDateTime) -> Unit = { }
    var getGameLogLogic: (key: LocalDateTime) -> List<Log>? = { emptyList() }
    var getLatestLogLogic: () -> List<Log>? = { emptyList() }

    override fun getLatestKey(): LocalDateTime {
        callGetLatestKeyCount += 1
        return getLatestKeyLogic()
    }

    override fun createGameLog(key: LocalDateTime) {
        callCreateGameLogCount += 1
        createGameLogLogic(key)
    }

    override fun addLog(key: LocalDateTime, log: Log) {
        callAddLogCount += 1
        addLogLogic(key, log)
    }

    override fun fixLogByEvolution(key: LocalDateTime) {
        callFixLogByEvolutionCount += 1
        fixLogByEvolutionLogic(key)
    }

    override fun getGameLog(key: LocalDateTime): List<Log>? {
        callGetGameLogCount += 1
        return getGameLogLogic(key)
    }

    override fun getLatestLog(): List<Log>? {
        callGetLatestLogCount += 1
        return getLatestLogLogic()
    }
}
