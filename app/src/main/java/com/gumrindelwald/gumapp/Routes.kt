package com.gumrindelwald.gumapp

class Routes {
    companion object {
        const val HOME = "home"
        const val INTRO = "intro"

        const val GUMRUN = "gumrun"
    }

    class Gumrun {
        companion object {
            const val ACTIVE_RUN = "gumrun_active_run"
            const val RUN_OVERVIEW = "gumrun_overview"

            const val ACTIVE_RUN_DEEP_LINK = "gumrun://active-run"
        }
    }
}