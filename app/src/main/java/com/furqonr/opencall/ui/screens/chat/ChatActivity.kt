package com.furqonr.opencall.ui.screens.chat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.furqonr.opencall.R
import com.furqonr.opencall.databinding.ActivityChatBinding
import com.furqonr.opencall.databinding.InputChatBinding
import com.furqonr.opencall.models.ChatModel
import com.furqonr.opencall.models.User
import com.furqonr.opencall.ui.utils.DateConverter
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var userInput: InputChatBinding
    private lateinit var viewModel: ChatViewModel
    private lateinit var chatId: String
    private var chats: MutableList<ChatModel> = mutableListOf()
    private var adapter: ChatAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        userInput = binding.layoutInput
        chatId = intent.getStringExtra("chatId").toString()

        viewModel = ChatViewModel()
        setMessage()
        val (senderUid, receiverUid) = chatId.split("::")
        viewModel.currentUser?.let {
            if (senderUid == it.uid) {
                setAppbar(receiverUid)
                chatInputHandler(receiverUid)
            } else {
                setAppbar(senderUid)
                chatInputHandler(senderUid)
            }
        }
        binding.appbar.backArrow.setOnClickListener {
            finish()
        }
    }

    private fun setMessage() {
        val layoutManager =
            LinearLayoutManager(applicationContext, LinearLayoutManager.VERTICAL, true)
        binding.messageLayout.layoutManager = layoutManager
        val listener = object : OnLongPressListener {
            override fun chat(chat: ChatModel) {
                println(chat)
            }
        }
        viewModel.getChats(chatId) { chatModels ->
            chats = chatModels.toMutableList()
            viewModel.currentUser?.let {
                adapter = ChatAdapter(chats, it, listener)
                binding.messageLayout.adapter = adapter
            }
        }
    }

    private fun setAppbar(userUid: String) {
        viewModel.getUser(userUid) {
            binding.appbar.title.text = it.displayName.capitalize(Locale.current)
            binding.appbar.status.text = if (it.status == "online") {
                it.status
            } else {
                DateConverter(it.status).convert()
            }
            Glide.with(applicationContext)
                .load(it.avatar)
                .placeholder(R.drawable.animal)
                .into(binding.appbar.avatar)
        }
    }

    private fun chatInputHandler(userUid: String) {
        var message = ""
        var currentUser: User? = null
        viewModel.currentUser?.let {
            currentUser = User(
                uid = it.uid,
                displayName = "${it.displayName}",
            )
        }
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, before: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                message = s?.toString().toString()
            }

            override fun afterTextChanged(s: Editable?) {
            }

        }
        viewModel.getUser(userUid) {
            userInput.editText.addTextChangedListener(watcher)
            userInput.sendMessage.setOnClickListener { _ ->
                currentUser?.let { user ->
                    viewModel.sendMessage(
                        message = message,
                        chatId = chatId,
                        sender = user.uid,
                        receiver = it.uid
                    ) { _ ->
                        binding.messageLayout.smoothScrollToPosition(0)
                    }
                }
                userInput.editText.setText("")
                message = ""
            }
        }

    }

}