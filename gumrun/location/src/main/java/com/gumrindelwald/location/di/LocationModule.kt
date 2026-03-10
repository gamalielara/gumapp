package com.gumrindelwald.location.di

import com.gumrindelwald.domain.LocationObserver
import com.gumrindelwald.location.AndroidLocationObserver
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val LocationModule = module {
    singleOf(::AndroidLocationObserver).bind<LocationObserver>()
}