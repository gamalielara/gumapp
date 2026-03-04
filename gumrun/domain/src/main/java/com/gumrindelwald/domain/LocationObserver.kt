package com.gumrindelwald.domain

import kotlinx.coroutines.flow.Flow

interface LocationObserver {
    fun observeLocation(interval: Long): Flow<RunLocation>
}