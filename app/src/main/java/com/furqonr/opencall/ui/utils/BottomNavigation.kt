package com.furqonr.opencall.ui.utils

sealed class BottomNavigation(val route: String) {
    object WelcomeScreen: BottomNavigation("welcome")
}
