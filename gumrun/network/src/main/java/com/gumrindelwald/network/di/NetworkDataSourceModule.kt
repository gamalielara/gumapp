package com.gumrindelwald.network.di

import com.gumrindelwald.domain.run.RemoteRunDataSource
import com.gumrindelwald.network.run_network_utils.KtorRemoteRunDataSource
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val NetworkDataSourceModule = module {
    singleOf(::KtorRemoteRunDataSource).bind<RemoteRunDataSource>()
}