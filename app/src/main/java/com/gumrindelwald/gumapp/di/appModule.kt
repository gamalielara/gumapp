package com.gumrindelwald.gumapp.di

import com.gumrindelwald.gumapp.GumAppModules
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val appModule = module {
    // Expose coroutine scope in the application scope
    // Koin will inject this to the classes that needs it, e.g. RunningTracker
    single<CoroutineScope> {
        (androidApplication() as GumAppModules).applicationScope
    }
}