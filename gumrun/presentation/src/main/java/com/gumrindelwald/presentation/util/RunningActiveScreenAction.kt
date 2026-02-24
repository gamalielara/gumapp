package com.gumrindelwald.presentation.util

sealed interface RunningActiveScreenAction {
    data object OnToggleRunStatus : RunningActiveScreenAction
    data object OnFinishRun : RunningActiveScreenAction
    data object OnResumeRun : RunningActiveScreenAction
    data object OnBackClick : RunningActiveScreenAction
    data object DismissRationaleDialog : RunningActiveScreenAction


}
