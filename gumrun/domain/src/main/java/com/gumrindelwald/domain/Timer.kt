package com.gumrindelwald.domain

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

object Timer {
    fun timeAndAEmit(): Flow<Duration> {
        return flow {
            var lastEmitTime = System.currentTimeMillis()

            while (true) {
                delay(1000L)
                val currentTime = System.currentTimeMillis()
                val elapsedTime = currentTime - lastEmitTime
                println("haha hehe $lastEmitTime, $currentTime, $elapsedTime, in millisec elapsed time: ${elapsedTime.milliseconds}")
                emit(elapsedTime.milliseconds)
                lastEmitTime = currentTime
            }
        }
    }
}