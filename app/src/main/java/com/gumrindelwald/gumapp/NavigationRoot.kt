package com.gumrindelwald.gumapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.gumrindelwald.presentation.GumrunDashboardRoot
import com.gumrindelwald.presentation.run_overview.GumRunOverviewScreenRoot

@Composable
fun NavigationRoot(
    navController: NavHostController,
) {
    // TODO
    NavHost(navController = navController, startDestination = Routes.GUMRUN) {
        gumrunGraph(navController)
    }
}

fun NavGraphBuilder.gumrunGraph(navController: NavHostController) {
    navigation(
        startDestination = Routes.Gumrun.RUN_OVERVIEW,
        route = Routes.GUMRUN
    ) {
        composable(
            route = Routes.Gumrun.ACTIVE_RUN, deepLinks = listOf(
                navDeepLink {
                    uriPattern = Routes.Gumrun.ACTIVE_RUN_DEEP_LINK
                }
            )
        ) {
            GumrunDashboardRoot(
                mainActivityClass = MainActivity::class.java,
                onRunFinished = {
                    navController.navigateUp()
                },
                onBack = {
                    navController.navigateUp()
                }
            )
        }
        composable(
            route = Routes.Gumrun.RUN_OVERVIEW
        ) {
            GumRunOverviewScreenRoot()
        }
    }
}