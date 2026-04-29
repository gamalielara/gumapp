package com.gumrindelwald.domain.run

import com.gumrindelwald.core.domain.util.DataError
import com.gumrindelwald.core.domain.util.Result
import kotlinx.coroutines.flow.Flow

typealias RunId = String

// Similar with DAO
interface LocalRunDataSource {
    fun getRuns(): Flow<List<Run>>
    suspend fun upsertRun(run: Run): Result<RunId, DataError.Local>
    suspend fun upsertRuns(runs: List<Run>): Result<List<RunId>, DataError.Local>
    suspend fun deleteRun(id: RunId)
    suspend fun deleteAllRuns()
}