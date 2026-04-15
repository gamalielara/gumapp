package com.gumrindelwald.gumrun.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.gumrindelwald.domain.run.RunId
import com.gumrindelwald.gumrun.database.entity.RunEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RunDao {
    @Upsert
    suspend fun upsertRun(run: RunEntity): RunId

    @Upsert
    suspend fun upsertRuns(runs: List<RunEntity>): RunId

    @Query("SELECT * FROM runentity ORDER BY dateTimeUTC DESC")
    fun getRuns(): Flow<List<RunEntity>>

    @Query("DELETE FROM runentity WHERE id = :id")
    fun deleteRun(id: String)

    @Query("DELETE FROM runentity")
    suspend fun deleteAllRuns()
}
