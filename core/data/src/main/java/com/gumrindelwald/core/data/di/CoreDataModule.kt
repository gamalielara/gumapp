package com.gumrindelwald.core.data.di

import com.gumrindelwald.core.data.EncryptedSessionStorage
import com.gumrindelwald.core.domain.util.SessionStorage
import com.gumrindelwald.core.network.HttpClientFactory
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val CoreDataModule = module {

    single {
        HttpClientFactory(get()).build()
    }
    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()
}