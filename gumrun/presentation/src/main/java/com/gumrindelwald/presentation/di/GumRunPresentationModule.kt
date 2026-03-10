package com.gumrindelwald.presentation.di

import com.gumrindelwald.domain.RunningTracker
import com.gumrindelwald.presentation.DashboardViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val GumRunPresentationModule = module {
    viewModelOf(::DashboardViewModel)
    singleOf(::RunningTracker)
}