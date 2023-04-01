package com.furqonr.opencall.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.furqonr.opencall.models.User
import com.furqonr.opencall.ui.components.dashboard.PublicUser
import com.furqonr.opencall.ui.theme.Typography


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Dashboard(
    navController: NavController
) {
    val viewModel: DashboardViewModel = viewModel()
    val (users, setUsers) = remember {
        mutableStateOf(listOf<User?>(null))
    }

    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues)
        ) {
            item(0) {
                Text(text = "Public user", modifier = Modifier.padding(4.dp), style = Typography.h6)
            }
            items(
                items = users,
                key = { it?.uid ?: "" }
            ) { item ->
                if (item != null) {
                    PublicUser(user = item, navController = navController, viewModel)
                }
            }
            item(1) {
                Text(text = "My Chats", modifier = Modifier.padding(4.dp), style = Typography.h6)
            }
        }

    }

    LaunchedEffect(users.size) {
        viewModel.getUsers {
            setUsers(it.take(5))
        }
    }

}