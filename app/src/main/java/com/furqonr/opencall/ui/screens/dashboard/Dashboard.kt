package com.furqonr.opencall.ui.screens.dashboard

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.furqonr.opencall.MainViewModel
import com.furqonr.opencall.ui.components.appbar.AppBar

@Composable
fun Dashboard(
    onBackPress: () -> Unit = {}
) {
    val viewModel: MainViewModel = viewModel()

    Scaffold(
        topBar = {
            AppBar(
                title = "Dashboard"
            )
        }
    ) {
        Column(
            modifier = Modifier.padding(it)
        ) {
            Text(text = "Dashboard")
            Button(onClick = {
                viewModel.signOut()
//                onBackPress()
            }) {
                Text(text = "Logout")
            }
        }
    }
    BackHandler(enabled = true, onBack = onBackPress)
}