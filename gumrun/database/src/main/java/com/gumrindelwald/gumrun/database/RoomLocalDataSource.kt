package com.gumrindelwald.gumrun.database

import android.database.sqlite.SQLiteFullException
import com.gumrindelwald.core.domain.util.DataError
import com.gumrindelwald.core.domain.util.Result
import com.gumrindelwald.domain.run.LocalRunDataSource
import com.gumrindelwald.domain.run.Run
import com.gumrindelwald.domain.run.RunId
import com.gumrindelwald.gumrun.database.dao.RunDao
import com.gumrindelwald.gumrun.database.mappers.toRun
import com.gumrindelwald.gumrun.database.mappers.toRunEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// TODO: Result helper
class RoomLocalDataSource(
    private val runDao: RunDao
) : LocalRunDataSource {
    override fun getRuns(): Flow<List<Run>> {
        return runDao.getRuns().map { runEntities ->
            runEntities.map { it -> it.toRun() }
        }
    }

    override suspend fun upsertRun(run: Run): Result<RunId, DataError.Local> {
        return try {
            val entity = run.toRunEntity()
            runDao.upsertRun(entity)
            Result.Success(entity.id)
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun upsertRuns(runs: List<Run>): Result<List<RunId>, DataError.Local> {
        return try {
            val entities = runs.map { it.toRunEntity() }
            runDao.upsertRuns(entities)
            Result.Success(entities.map { it.id })
        } catch (e: SQLiteFullException) {
            Result.Error(DataError.Local.DISK_FULL)
        }
    }

    override suspend fun deleteRun(id: RunId) {
        runDao.deleteRun(id)
    }

    override suspend fun deleteAllRuns() {
        runDao.deleteAllRuns()
    }
}