package com.gumrindelwald.data

import com.gumrindelwald.core.domain.util.DataError
import com.gumrindelwald.core.domain.util.EmptyResult
import com.gumrindelwald.core.domain.util.Result
import com.gumrindelwald.core.domain.util.asEmptyDataResult
import com.gumrindelwald.domain.run.LocalRunDataSource
import com.gumrindelwald.domain.run.RemoteRunDataSource
import com.gumrindelwald.domain.run.Run
import com.gumrindelwald.domain.run.RunRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow

class OfflineFirstRunRepository(
    private val localRunDataSource: LocalRunDataSource,
    private val remoteRunDataSource: RemoteRunDataSource,
    private val applicationScope: CoroutineScope
) : RunRepository {
    // Local database will be the source of truth
    override fun getRuns(): Flow<List<Run>> {
        return localRunDataSource.getRuns()
    }

    // Cancellable when the coroutine is cancelled (e.g. when navigates away from the screen and view model is destroyed)
    // Might happen during network call and saving data to local database
    // Solution 1: use withContext(NonCancellable) to make sure the coroutine is not cancelled (not encouraged)
    // Solution 2: use application scope context
    override suspend fun fetchRuns(): EmptyResult<DataError> {
        return when (val result = remoteRunDataSource.getRuns()) {
            is Result.Error -> result.asEmptyDataResult()
            is Result.Success -> {
                // Upsert the fetched run to local database
                // Once updated the flow returned from `getRuns` will trigger
                applicationScope.async {
                    localRunDataSource.upsertRuns(result.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun upsertRun(
        run: Run,
        mapPicture: ByteArray
    ): EmptyResult<DataError> {
        val localResult = localRunDataSource.upsertRun(run)

        if (localResult !is Result.Success) {
            return localResult.asEmptyDataResult()
        }

        val runWithId = run.copy(id = localResult.data)
        val remoteRunResult = remoteRunDataSource.postRun(runWithId, mapPicture)

        return when (remoteRunResult) {
            // TODO: Handle error
            is Result.Error -> remoteRunResult.asEmptyDataResult()
            is Result.Success -> {
                applicationScope.async {
                    localRunDataSource.upsertRun(remoteRunResult.data).asEmptyDataResult()
                }.await()
            }
        }
    }

    override suspend fun deleteRun(runId: String) {
        localRunDataSource.deleteRun(runId)

        val remoteResult = applicationScope.async {
            remoteRunDataSource.deleteRun(runId)
        }.await()

    }
}