package com.gumrindelwald.database

import androidx.room.Entity

@Entity(tableName = "run_table")
data class RunEntity(
    val id: String,
)
