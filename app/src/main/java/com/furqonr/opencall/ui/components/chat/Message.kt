package com.furqonr.opencall.ui.components.chat

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.furqonr.opencall.MainViewModel
import com.furqonr.opencall.R
import com.furqonr.opencall.models.ChatModel
import com.furqonr.opencall.ui.screens.chat.ChatViewModel
import java.text.DateFormat
import kotlin.math.min


@Composable
fun Message(
    chatModel: ChatModel,
    videModel: ChatViewModel,
    chatId: String,
) {
    val chatStatus = remember {
        mutableStateOf(false)
    }
    val screenWidth = LocalConfiguration.current.screenWidthDp
    val width = min(screenWidth * 0.7f, 300f).dp
    val isCurrentUser = videModel.currentUser?.uid == chatModel.sender.uid

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (isCurrentUser) Arrangement.Start else Arrangement.End,
        ) {
            Row(
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier
                    .widthIn(min = 0.dp, max = width)
                    .padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Column(
                    horizontalAlignment = if (isCurrentUser) Alignment.Start else Alignment.End,
                ) {
                    Card(
                        modifier = Modifier.padding(8.dp),
                        shape = RoundedCornerShape(
                            topEnd = 8.dp,
                            topStart = 8.dp,
                            bottomEnd = if (isCurrentUser) 8.dp else 0.dp,
                            bottomStart = if (isCurrentUser) 0.dp else 8.dp
                        ),
                    ) {
                        Text(
                            text = chatModel.message,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        Text(
                            text = DateFormat.getDateInstance().format(chatModel.timestamp),
                            fontSize = 12.sp
                        )
                        if (!chatStatus.value) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_outline_watch_later_24),
                                contentDescription = "Message status is delivered",
                                modifier = Modifier.size(14.dp),
                                tint = Color.Gray
                            )
                        } else {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_check_24),
                                contentDescription = "Message status is sent",
                                modifier = Modifier.size(14.dp),
                                tint = Color.Gray
                            )
                        }
                    }
                }
            }
        }
    }

    SideEffect {
        videModel.getMessageStatus(chatId = chatId, messageId = chatModel.uid) {
            chatStatus.value = it

        }
    }
}