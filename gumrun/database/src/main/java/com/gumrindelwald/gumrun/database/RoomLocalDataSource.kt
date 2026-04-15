package com.gumrindelwald.gumrun.database

import android.database.sqlite.SQLiteFullException
import com.gumrindelwald.domain.run.LocalRunDatabase
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
) : LocalRunDatabase {
    override fun getRuns(): Flow<List<Run>> {
        return runDao.getRuns().map { runEntities ->
            runEntities.map { it -> it.toRun() }
        }
    }

    override suspend fun upsertRun(run: Run): RunId {
        return try {
            runDao.upsertRun(run.toRunEntity())
        } catch (e: SQLiteFullException) {
            return ""
        }
    }

    override suspend fun upsertRuns(runs: List<Run>): RunId {
        return try {
            val entities = runs.map { it.toRunEntity() }
            runDao.upsertRuns(entities)
        } catch (e: SQLiteFullException) {
            return ""
        }
    }

    override suspend fun deleteRun(id: RunId) {
        runDao.deleteRun(id)
    }

    override suspend fun deleteAllRuns() {
        runDao.deleteAllRuns()
    }
}