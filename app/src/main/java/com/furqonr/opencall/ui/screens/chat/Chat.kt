package com.furqonr.opencall.ui.screens.chat

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.furqonr.opencall.models.ChatModel
import com.furqonr.opencall.models.User
import com.furqonr.opencall.ui.components.chat.ChatInput
import com.furqonr.opencall.ui.components.chat.ChatTopBar
import com.furqonr.opencall.ui.components.chat.Message
import kotlinx.coroutines.launch


@Composable
fun Chat(
    navController: NavController,
    chatId: String
) {

    val model: ChatViewModel = viewModel()
    val (userId, senderId) = chatId.split("::")
    val (user, setUser) = remember {
        mutableStateOf<User?>(null)
    }
    val (sender, setSender) = remember {
        mutableStateOf<User?>(null)
    }

    val (currentUserUid, setCurrentUserUid) = remember {
        mutableStateOf("")
    }

    val (otherUserUid, setOtherUserUid) = remember {
        mutableStateOf("")
    }

    val (chats, setChats) = remember {
        mutableStateOf<List<ChatModel>>(mutableListOf())
    }

    val lazyColumnState = rememberLazyListState()

    val scope = rememberCoroutineScope()



    Scaffold(
        topBar = {
            ChatTopBar(
                navController = navController,
                user = user
            )
        },
        bottomBar = {
            ChatInput(
                onSent = { message ->
                    model.sendMessage(
                        chatId = chatId,
                        message = message,
                        sender = sender!!,
                        receiver = user!!
                    ) {
                        setChats(chats + it)
                        scope.launch {
                            lazyColumnState.scroll {
                                scrollBy(
                                    pixels = Float.MAX_VALUE,
                                )
                            }
                        }
                    }

                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .padding(paddingValues),
            reverseLayout = true,
            state = lazyColumnState,
        ) {
            item {
                Text(text = "", modifier = Modifier.padding(0.dp), fontSize = 0.sp)
            }
            items(chats) { chat ->
                Message(
                    chatModel = chat,
                    videModel = model,
                    chatId = chatId,
                    userId = currentUserUid
                )
            }
        }
    }

    SideEffect {
        model.currentUser?.uid?.let { owner ->
            if (owner == userId) {
                setCurrentUserUid(userId)
                setOtherUserUid(senderId)
            } else {
                setCurrentUserUid(senderId)
                setOtherUserUid(userId)
            }
            model.getUser(uid = userId) {
                setUser(it)
            }
            model.getUser(uid = senderId) {
                setSender(it)
            }
            model.getChats(chatId) {
                setChats(it)
            }
        }
    }

}

