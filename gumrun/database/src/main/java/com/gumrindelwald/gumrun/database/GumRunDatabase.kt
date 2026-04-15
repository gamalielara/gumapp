package com.gumrindelwald.gumrun.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gumrindelwald.gumrun.database.dao.RunDao
import com.gumrindelwald.gumrun.database.entity.RunEntity

@Database(entities = [RunEntity::class], version = 1)
abstract class GumRunDatabase : RoomDatabase() {
    abstract val runDao: RunDao
}
