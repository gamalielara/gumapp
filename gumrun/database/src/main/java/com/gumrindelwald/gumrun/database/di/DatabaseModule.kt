package com.gumrindelwald.gumrun.database.di

import androidx.room.Room
import com.gumrindelwald.domain.run.LocalRunDatabase
import com.gumrindelwald.gumrun.database.GumRunDatabase
import com.gumrindelwald.gumrun.database.RoomLocalDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val DatabaseModule = module {
    single {
        Room.databaseBuilder(
            androidApplication(),
            GumRunDatabase::class.java,
            "gumrun.db"
        ).build()
    }

    single {
        get<GumRunDatabase>().runDao
    }

    singleOf(::RoomLocalDataSource).bind<LocalRunDatabase>()
}