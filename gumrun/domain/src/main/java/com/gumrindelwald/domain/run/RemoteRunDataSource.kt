package com.gumrindelwald.domain.run

import com.gumrindelwald.core.domain.util.DataError
import com.gumrindelwald.core.domain.util.EmptyResult
import com.gumrindelwald.core.domain.util.Result

interface RemoteRunDataSource {
    suspend fun getRuns(): Result<List<Run>, DataError.Network>
    suspend fun postRun(run: Run, mapPicture: ByteArray): Result<Run, DataError.Network>
    suspend fun deleteRun(runId: String): EmptyResult<DataError.Network>
}