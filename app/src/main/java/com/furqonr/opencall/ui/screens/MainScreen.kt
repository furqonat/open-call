package com.furqonr.opencall.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import com.furqonr.opencall.MainViewModel
import com.furqonr.opencall.ui.components.appbar.AppBar

@Composable
fun MainScreen(
    onBackPress: () -> Unit = {},
    userState: (Boolean) -> Unit
) {
    val viewModel: MainViewModel = viewModel()

    val (currentUser) = viewModel.currentUser

    Scaffold(
        topBar = {
            AppBar()
        },
        bottomBar = {

        }
    )

    LaunchedEffect(key1 = currentUser) {
        userState(currentUser)
    }

    BackHandler(enabled = true, onBack = onBackPress)
}