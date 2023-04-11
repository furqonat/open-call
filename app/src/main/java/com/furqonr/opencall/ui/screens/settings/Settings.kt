package com.furqonr.opencall.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.furqonr.opencall.MainViewModel

@Composable
fun Settings() {
    val model: MainViewModel = viewModel()
    fun onSignOut() = run { model.signOut() }
    Scaffold { paddingValue ->
        Column(modifier = Modifier.padding(paddingValue)) {
            Button(onClick = {onSignOut()}) {
                Text("Keluar")
            }
        }
    }
}