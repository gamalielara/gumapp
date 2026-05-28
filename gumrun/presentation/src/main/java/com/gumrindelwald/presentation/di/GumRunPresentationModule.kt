package com.gumrindelwald.presentation.di

import com.gumrindelwald.domain.RunningTracker
import com.gumrindelwald.presentation.screens.active_run.ActiveRunScreenViewModel
import com.gumrindelwald.presentation.screens.run_overview.RunOverviewViewModel
import com.gumrindelwald.presentation.util.RunningStatusTracker
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val GumRunPresentationModule = module {
    viewModelOf(::ActiveRunScreenViewModel)
    viewModelOf(::RunOverviewViewModel)
    singleOf(::RunningTracker)
    singleOf(::RunningStatusTracker)

}