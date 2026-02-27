package com.gumrindelwald.domain.location

import com.gumrindelwald.domain.RunLocation
import kotlinx.coroutines.flow.Flow

interface LocationObserver {
    fun observeLocation(interval: Long): Flow<RunLocation>
}