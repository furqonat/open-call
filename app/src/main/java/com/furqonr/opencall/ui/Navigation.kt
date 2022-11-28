package com.furqonr.opencall.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.furqonr.opencall.MainActivity
import com.furqonr.opencall.MainViewModel
import com.furqonr.opencall.ui.screens.dashboard.Dashboard
import com.furqonr.opencall.ui.screens.intro.Detail
import com.furqonr.opencall.ui.screens.intro.Intro
import com.furqonr.opencall.ui.screens.intro.Signin
import com.furqonr.opencall.ui.utils.Introduction
import com.furqonr.opencall.ui.utils.Screens


@Composable
fun Navigation() {
    val context = (LocalContext.current as MainActivity)
    val navController: NavHostController = rememberNavController()

    val mainViewModel: MainViewModel = viewModel()

    val defaultScreen =
        if (mainViewModel.currentUser.value) Screens.MAIN_SCREEN.route
        else Screens.INTROS.route

    NavHost(navController = navController, startDestination = defaultScreen) {
        introduction(navigation = navController, viewModel = mainViewModel)
        composable(Screens.MAIN_SCREEN.route) {
            Dashboard {
                if (mainViewModel.currentUser.value) {
                    context.finish()
                } else {
                    navController.navigate(Screens.INTROS.route)
                }
            }
        }
    }
}

fun NavGraphBuilder.introduction(
    startDestination: String = Introduction.intro.destination,
    route: String = Screens.INTROS.route,
    navigation: NavHostController,
    viewModel: MainViewModel
) {
    navigation(
        startDestination = startDestination,
        route = route
    ) {
        composable(Introduction.intro.destination) {
            Intro {
                navigation.navigate(Introduction.detail.destination)
            }
        }
        composable(Introduction.detail.destination) {
            Detail { navigation.navigate(Introduction.signin.destination) }
        }
        composable(Introduction.signin.destination) {
            Signin(
                onSignInClick = { username ->
                    if (username != null) {
                        viewModel.signIn(username) {
                            if (it != null) {
                                navigation.navigate(Screens.MAIN_SCREEN.route)
                            }
                        }
                    }
                },
                onGoogleClick = {
                    navigation.navigate(Screens.MAIN_SCREEN.route)
                }
            )
        }
    }
}