package com.gumrindelwald.domain.run

import kotlinx.coroutines.flow.Flow

typealias RunId = String

// Similar with DAO
interface LocalRunDatabase {
    fun getRuns(): Flow<List<Run>>
    suspend fun upsertRun(run: Run): RunId
    suspend fun upsertRuns(runs: List<Run>): RunId
    suspend fun deleteRun(id: RunId)
    suspend fun deleteAllRuns()
}