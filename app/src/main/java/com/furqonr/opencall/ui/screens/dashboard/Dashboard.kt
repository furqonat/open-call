package com.furqonr.opencall.ui.screens.dashboard

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.furqonr.opencall.R
import com.furqonr.opencall.models.User
import com.furqonr.opencall.ui.components.dashboard.PublicUser
import com.furqonr.opencall.ui.theme.Typography
import java.util.*


@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun Dashboard(
    navController: NavController
) {
    val viewModel: DashboardViewModel = viewModel()
    val (users, setUsers) = remember {
        mutableStateOf(listOf<User?>(null))
    }
    val currentUser = remember {
        mutableStateOf(viewModel.currentUser.value)
    }
//    val defaultAvatar = if (currentUser.value?.displayName?.isEmpty() == false) {
//        currentUser.value?.displayName?.first().toString()
//            .uppercase(Locale.getDefault())
//    } else {
//        ""
//    }
    val defaultAvatar = ""


    fun onProfileClick() = run { navController.navigate("profile") }

    Scaffold(
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(id = R.string.chat),
                        style = Typography.h5
                    )
                }
                Surface(
                    shape = CircleShape,
                    modifier = Modifier
                        .padding(4.dp)
                        .size(40.dp)
                        .clickable { onProfileClick() },
                    border = BorderStroke(1.dp, color = Color.Gray),
                    color = Color.Gray
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        if (currentUser.value?.photoUrl != null) {
//                            Image(painter =, contentDescription = "profile picture")
                            GlideImage(
                                model = currentUser.value?.photoUrl,
                                contentDescription = ""
                            )
                        } else {
                            Text(
                                text = defaultAvatar,
                                style = Typography.h5,
                                color = Color.White,
                                textAlign = TextAlign.Center,
                            )
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
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