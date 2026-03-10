package com.gumrindelwald.gumapp

import android.app.Application
import com.gumrindelwald.gumapp.di.appModule
import com.gumrindelwald.location.di.LocationModule
import com.gumrindelwald.presentation.di.GumRunPresentationModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class GumAppModules : Application() {
    // Created this here to match the application lifetime/lifecycle
    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@GumAppModules)
            modules(
                appModule,
                GumRunPresentationModule,
                LocationModule
            )
        }
    }
}