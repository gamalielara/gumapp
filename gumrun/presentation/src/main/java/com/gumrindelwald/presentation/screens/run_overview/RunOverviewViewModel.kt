package com.gumrindelwald.presentation.screens.run_overview

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gumrindelwald.domain.run.RunRepository
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class RunOverviewViewModel(
    private val runRepository: RunRepository
) : ViewModel() {
    var state by mutableStateOf(RunOverviewState())
        private set

    init {
        runRepository.getRuns().onEach { runs ->
            val runUI = runs.map { it.toRunUI() }
            state = state.copy(runs = runUI)
        }.launchIn(viewModelScope)

        // Fetch runs from remote database, the flow will be triggered again
        viewModelScope.launch {
            runRepository.fetchRuns()
        }
    }

    fun onAction(action: RunOverviewAction) {
        when (action) {
            is RunOverviewAction.DeleteRun -> {
                viewModelScope.launch {
                    runRepository.deleteRun(action.run.id)
                }
            }

            else -> Unit
        }
    }
}