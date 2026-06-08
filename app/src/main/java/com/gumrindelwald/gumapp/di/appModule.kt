package com.gumrindelwald.gumapp.di

import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
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

    // Encrypted SharedPreferences used by EncryptedSessionStorage
    single<SharedPreferences> {
        EncryptedSharedPreferences.create(
            androidApplication(),
            "auth_pref",
            MasterKey.Builder(androidApplication())
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }
}