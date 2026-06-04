package com.gumrindelwald.data.di

import com.gumrindelwald.data.OfflineFirstRunRepository
import com.gumrindelwald.domain.run.RunRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val GumrunDataModule = module {
    singleOf(::OfflineFirstRunRepository).bind<RunRepository>()
}