package com.furqonr.opencall.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.furqonr.opencall.ui.screens.intro.Detail
import com.furqonr.opencall.ui.screens.intro.Welcome
import com.furqonr.opencall.ui.utils.Introduction
import com.furqonr.opencall.ui.utils.Screens

@Composable
fun Navigation(
    defaultScreen: String = Screens.WELCOME.route
) {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = defaultScreen) {
        welcome(navigation = navController)
    }
}

fun NavGraphBuilder.welcome(
    startDestination: String = Introduction.intro.destination,
    route: String = Screens.WELCOME.route,
    navigation: NavHostController
) {
    navigation(
        startDestination = startDestination,
        route = route
    ) {
        composable(Introduction.intro.destination) {
            Welcome {
                navigation.navigate(Introduction.detail.destination)
            }
        }
        composable(Introduction.detail.destination) {
            Detail()
        }
    }
}