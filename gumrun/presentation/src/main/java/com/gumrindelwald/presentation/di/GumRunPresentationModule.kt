package com.gumrindelwald.presentation.di

import com.gumrindelwald.presentation.DashboardViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module


val GumRunPresentationModule = module {
    viewModelOf(::DashboardViewModel))
}