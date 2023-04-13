package com.furqonr.opencall.ui.screens.chat

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.furqonr.opencall.models.ChatModel
import com.furqonr.opencall.models.User
import com.furqonr.opencall.ui.components.chat.ChatInput
import com.furqonr.opencall.ui.components.chat.ChatTopBar
import com.furqonr.opencall.ui.components.chat.Message
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
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


    val (chats, setChats) = remember {
        mutableStateOf<List<ChatModel>>(mutableListOf())
    }


    val scope = rememberCoroutineScope()

    val scrollState = rememberLazyListState()

    val focusRequester = remember { FocusRequester() }


    Scaffold(
        topBar = {
            ChatTopBar(
                navController = navController,
                user = user,
            )
        },
        bottomBar = {
            ChatInput(
                onSent = { message ->
                    model.sendMessage(
                        chatId = chatId,
                        message = message,
                        sender = sender!!.uid,
                        receiver = user!!.uid
                    ) {
                        scope.launch {
                            scrollState.animateScrollToItem(scrollState.firstVisibleItemScrollOffset)
                        }
                    }

                }
            )
        }
    ) { paddingValues ->

           LazyColumn(modifier = Modifier.padding(paddingValues), state = scrollState) {
               items(chats) {
                   Message(
                       chatModel = it,
                       videModel = model,
                       chatId = chatId,
                   )
               }
               item(1) {

               }
           }
    }

    SideEffect {
        model.currentUser?.uid?.let { owner ->
            if (owner == userId) {
                setCurrentUserUid(userId)
            } else {
                setCurrentUserUid(senderId)
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

