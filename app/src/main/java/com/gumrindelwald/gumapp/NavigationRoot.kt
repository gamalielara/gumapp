package com.gumrindelwald.gumapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.gumrindelwald.presentation.GumrunDashboardRoot

@Composable
fun NavigationRoot(
    navController: NavHostController,
    isLoggedIn: Boolean
) {
    NavHost(navController = navController, startDestination = Routes.HOME) {
        authGraph()
        gumrunGraph()
    }
}

fun NavGraphBuilder.authGraph() {
    // TODO
}

fun NavGraphBuilder.gumrunGraph() {
    navigation(
        startDestination = Routes.Gumrun.ACTIVE_RUN,
        route = Routes.GUMRUN
    ) {
        composable(
            route = Routes.Gumrun.ACTIVE_RUN, deepLinks = listOf(
                navDeepLink {
                    uriPattern = Routes.Gumrun.ACTIVE_RUN_DEEP_LINK
                }
            )) {
            GumrunDashboardRoot(
                mainActivityClass = MainActivity::class.java,
                onRunFinished = { },
                onBack = { }
            )
        }
    }
}