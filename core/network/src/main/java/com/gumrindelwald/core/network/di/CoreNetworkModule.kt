package com.gumrindelwald.core.network.di

import com.gumrindelwald.core.network.HttpClientFactory
import org.koin.dsl.module

val coreNetworkModule = module {
    single { HttpClientFactory(get()).build() }
}
