package com.furqonr.opencall.ui.components.dashboard

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.furqonr.opencall.models.User
import com.furqonr.opencall.ui.screens.dashboard.DashboardViewModel
import com.furqonr.opencall.ui.theme.Typography
import com.furqonr.opencall.ui.utils.DateConverter
import java.util.*

private val randomColor = listOf(
    Color.Blue,
    Color.Red,
    Color.Green,
    Color.Yellow,
    Color.Cyan,
    Color.Magenta,
    Color.DarkGray,
    Color.Black,
)

@Composable
fun PublicUser(
    user: User,
    navController: NavController,
    viewModel: DashboardViewModel,
) {
    val chatId = remember {
        mutableStateOf("")
    }
    val currentUserUid = remember {
        mutableStateOf("")
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
    ) {
        Surface(
            color = randomColor[Random().nextInt(randomColor.size)],
            modifier = Modifier
                .padding(4.dp)
                .size(50.dp),
            shape = CircleShape
        ) {
            Text(
                text = user.displayName.first().toString().uppercase(Locale.getDefault()),
                style = Typography.h4,
                color = Color.White,
                textAlign = TextAlign.Center,
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    if (chatId.value.isNotEmpty()) {
                        navController.navigate("chat/${chatId.value}")
                    }
                }
        ) {
            Text(text = user.displayName.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }, style = Typography.h6)
            user.status.let {
                if (it == "online") {
                    Text(text = it, style = Typography.caption)
                } else {
                    Text(text = DateConverter(it).convert(), style = Typography.caption)
                }
            }
        }
    }

    SideEffect {
        viewModel.currentUser.value?.uid?.let {
            currentUserUid.value = it
        }
    }

    LaunchedEffect(currentUserUid) {
        if (currentUserUid.value.isNotEmpty() && chatId.value.isEmpty()) {
            Log.e("TAG", "PublicUser: ${currentUserUid.value}")
            viewModel.getConversation(currentUserUid.value, user.uid) {
                chatId.value = it
            }
        }
    }
}