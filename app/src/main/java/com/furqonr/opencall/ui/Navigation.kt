package com.furqonr.opencall.ui

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.furqonr.opencall.MainViewModel
import com.furqonr.opencall.ui.screens.MainScreen
import com.furqonr.opencall.ui.screens.chat.Chat
import com.furqonr.opencall.ui.screens.intro.Detail
import com.furqonr.opencall.ui.screens.intro.Intro
import com.furqonr.opencall.ui.screens.intro.SignIn
import com.furqonr.opencall.ui.screens.profile.Profile
import com.furqonr.opencall.ui.utils.Introduction
import com.furqonr.opencall.ui.utils.Screens


@Composable
fun Navigation() {
    val navController: NavHostController = rememberNavController()

    val mainViewModel: MainViewModel = viewModel()

    val defaultScreen = if (mainViewModel.currentUser.value) Screens.MAIN_SCREEN.route
    else Screens.INTROS.route

    NavHost(navController = navController, startDestination = defaultScreen) {
        introduction(navigation = navController, viewModel = mainViewModel)
        composable(Screens.MAIN_SCREEN.route) {
            MainScreen(navController = navController)
        }

        composable("chat/{chatId}") { backStackEntry ->
            val chatId = backStackEntry.arguments?.getString("chatId")
            if (chatId != null) {
                Chat(navController = navController, chatId = chatId)
            }
        }
        composable("profile") {
            Profile()
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
        startDestination = startDestination, route = route
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
            SignIn(disabled = viewModel.loadingCreateNewUser.value, onSignInClick = { username ->
                if (username != null) {
                    viewModel.signIn(username) {
                        if (it != null) {
                            navigation.navigate(Screens.MAIN_SCREEN.route)
                        }
                    }
                }
            }, onGoogleClick = { authResult ->
                authResult.user?.let { it1 ->
                    viewModel.signInWithGoogle(it1) {
                        if (it != null) {
                            navigation.navigate(Screens.MAIN_SCREEN.route)
                        }
                    }
                }
            })
        }

    }
}