package com.furqonr.opencall.ui.screens.chat

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.Locale
import androidx.emoji2.emojipicker.RecentEmojiAsyncProvider
import androidx.emoji2.emojipicker.RecentEmojiProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.furqonr.opencall.R
import com.furqonr.opencall.databinding.ActivityChatBinding
import com.furqonr.opencall.databinding.InputChatBinding
import com.furqonr.opencall.models.ChatModel
import com.furqonr.opencall.models.User
import com.furqonr.opencall.ui.utils.DateConverter
import com.google.common.util.concurrent.Futures
import com.google.common.util.concurrent.ListenableFuture

class ChatActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBinding
    private lateinit var userInput: InputChatBinding
    private lateinit var viewModel: ChatViewModel
    private lateinit var chatId: String

    //    private lateinit var emojiPopup: EmojiPopup
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
        binding.layoutInput.editText.requestFocus()
        binding.layoutInput.editText.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.layoutInput.emojiPicker.visibility = View.GONE
            }
        }
        val keyboard = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.layoutInput.emoticon.setOnClickListener {
            val emoji = binding.layoutInput.emojiPicker
            if (emoji.visibility == View.GONE) {
                emoji.visibility = View.VISIBLE
                emoji.requestFocus()
                keyboard.hideSoftInputFromWindow(binding.layoutInput.editText.windowToken, InputMethodManager.HIDE_IMPLICIT_ONLY)
            } else {
                emoji.visibility = View.GONE
                emoji.clearFocus()
                binding.layoutInput.editText.requestFocus()
                keyboard.showSoftInput(binding.layoutInput.editText, InputMethodManager.SHOW_IMPLICIT)
            }
        }

        binding.layoutInput.emojiPicker.setRecentEmojiProvider(
            CustomRecentEmojiProvider(
                applicationContext
            )
        )
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
                    if (message.isEmpty() || message.isBlank()) return@setOnClickListener
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

internal class CustomRecentEmojiProvider(
    context: Context
) : RecentEmojiAsyncProvider, RecentEmojiProvider {
    companion object {
        private const val PREF_KEY_CUSTOM_EMOJI_FREQ = "pref_key_custom_emoji_freq"
        private const val RECENT_EMOJI_LIST_FILE_NAME =
            "androidx.emoji2.emojipicker.sample.preferences"
        private const val SPLIT_CHAR = ","
        private const val KEY_VALUE_DELIMITER = "="
    }

    private val sharedPreferences =
        context.getSharedPreferences(RECENT_EMOJI_LIST_FILE_NAME, Context.MODE_PRIVATE)
    private val emoji2Frequency: MutableMap<String, Int> by lazy {
        sharedPreferences.getString(PREF_KEY_CUSTOM_EMOJI_FREQ, null)?.split(SPLIT_CHAR)
            ?.associate { entry ->
                entry.split(KEY_VALUE_DELIMITER, limit = 2).takeIf { it.size == 2 }
                    ?.let { it[0] to it[1].toInt() } ?: ("" to 0)
            }?.toMutableMap() ?: mutableMapOf()
    }

    override fun getRecentEmojiListAsync(): ListenableFuture<List<String>> =
        Futures.immediateFuture(emoji2Frequency.toList().sortedByDescending { it.second }
            .map { it.first })

    override suspend fun getRecentEmojiList(): List<String> {
//        TODO("Not yet implemented")
        return emoji2Frequency.toList().sortedByDescending { it.second }
            .map { it.first }
    }

    override fun recordSelection(emoji: String) {
        emoji2Frequency[emoji] = (emoji2Frequency[emoji] ?: 0) + 1
        saveToPreferences()
    }

    private fun saveToPreferences() {
        sharedPreferences
            .edit()
            .putString(PREF_KEY_CUSTOM_EMOJI_FREQ, emoji2Frequency.entries.joinToString(SPLIT_CHAR))
            .commit()
    }
}